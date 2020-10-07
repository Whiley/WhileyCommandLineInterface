// Copyright 2011 The Whiley Project Developers
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package wycc.util;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import wybs.util.AbstractCompilationUnit.Value;
import wycc.cfg.ConfigFile;
import wycc.cfg.Configuration;
import wycc.lang.Command;
import wycc.lang.Package;
import wycc.lang.SemanticVersion;
import wyfs.lang.Content;
import wyfs.lang.Path;
import wyfs.lang.Path.Root;
import wyfs.util.DirectoryRoot;
import wyfs.util.Trie;
import wyfs.util.ZipFile;
import wyfs.util.ZipFileRoot;

/**
 *
 * @author djp
 *
 */
public class LocalPackageRepository implements Package.Repository {

	public static final Trie REPOSITORY_DIR = Trie.fromString("repository/dir");

	/**
	 * Schema for global configuration (i.e. which applies to all projects for a given user).
	 */
	public static Configuration.Schema SCHEMA = Configuration
			.fromArray(Configuration.UNBOUND_STRING(REPOSITORY_DIR, "local directory", false));

	protected final Command.Environment environment;
	protected final Package.Repository parent;
	protected final Content.Registry registry;
	protected final Path.Root root;

	public LocalPackageRepository(Command.Environment environment, Content.Registry registry, Path.Root root) throws IOException {
		this(environment,null,registry,root);
	}

	public LocalPackageRepository(Command.Environment environment, Package.Repository parent, Content.Registry registry, Path.Root root) throws IOException {
		this.parent = parent;
		this.registry = registry;
		this.environment = environment;
		// Check whether URL configuration given
		if(environment.hasKey(REPOSITORY_DIR)) {
			// Yes, therefore override default location
			String dir = environment.get(Value.UTF8.class, REPOSITORY_DIR).toString();
			this.root = new DirectoryRoot(dir, registry);
		} else {
			this.root = root;
		}
	}

	@Override
	public Package.Repository getParent() {
		return parent;
	}

	@Override
	public Set<SemanticVersion> list(String pkg) throws IOException {
		Set<Path.ID> matches = root.match(Content.filter("*", ZipFile.ContentType));
		HashSet<SemanticVersion> versions = new HashSet<>();
		String prefix = pkg + "-v";
		for(Path.ID m : matches) {
			// FIXME: need for m.last() seems like bug
			String str = m.last().toString();
			if(str.startsWith(prefix)) {
				SemanticVersion v = new SemanticVersion(str.substring(prefix.length()));
				versions.add(v);
			}
		}
		return versions;
	}

	@Override
	public Path.Root get(String pkg, SemanticVersion version) throws IOException {
		Trie id = Trie.fromString(pkg + "-v" + version);
		// Attempt to resolve it.
		if (!root.exists(id, ZipFile.ContentType)) {
			environment.getLogger().logTimedMessage("Failed loading  " + pkg + "-v" + version, 0, 0);
			return null;
		} else {
			// Extract entry for ZipFile
			Path.Entry<ZipFile> zipfile = root.get(id, ZipFile.ContentType);
			// Construct root representing this ZipFile
			return new ZipFileRoot(zipfile, registry);
		}
	}

	@Override
	public void put(ZipFile pkg, String name, SemanticVersion version) throws IOException {
		// Determine fully qualified package name
		Trie qpn = Trie.fromString(name + "-v" + version);
		// Dig out the file!
		Path.Entry<ZipFile> entry = root.create(qpn, ZipFile.ContentType);
		// Write the contents
		entry.write(pkg);
		// Flush
		entry.flush();
		//
		environment.getLogger().logTimedMessage("Installed " + entry.location(), 0, 0);
	}

}
