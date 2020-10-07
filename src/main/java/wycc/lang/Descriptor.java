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
package wycc.lang;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents meta-information about modules. That includes its version,
 * location, and dependencies. This information is obtained from the module.xml
 * file required for each module.
 *
 * @author David J. Pearce
 *
 */
public class Descriptor {
	/**
	 * The module name, which should provide a human-readable descriptive name
	 * for this module.
	 */
	private String name;

	/**
	 * The module identifier, which should be unique for this module (upto
	 * versioning).
	 */
	private final String id;

	/**
	 * The version number of this module, which is a triple of the form
	 * (major,minor,micro) and usually written major.minor.micro (e.g. 1.0.3).
	 */
	private final SemanticVersion version;

	/**
	 * The location of the module jar file.
	 */
	private final URL location;

	/**
	 * The name of the class responsible for activating this module.
	 */
	private final String activator;

	/**
	 * The list of other modules that this module depends upon. These modules
	 * must be loaded before this module can be loaded.
	 */
	private ArrayList<SemanticDependency> dependencies;

	public Descriptor(String name, String id, SemanticVersion version, URL location,
			String activator, List<SemanticDependency> dependencies) {
		this.name = name;
		this.id = id;
		this.version = version;
		this.location = location;
		this.activator = activator;
		this.dependencies = new ArrayList<>(dependencies);
	}

	/**
	 * Get the name of this module
	 *
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the id of this module
	 *
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * Get the version of this module
	 *
	 * @return
	 */
	public SemanticVersion getVersion() {
		return version;
	}

	/**
	 * Get the name of the activator class for this module. This class is
	 * instantiated when the module begins and used to control the start-up and
	 * shutdown of the module.
	 *
	 * @return
	 */
	public String getActivator() {
		return activator;
	}

	/**
	 * Return the location of the module jar.
	 *
	 * @return
	 */
	public URL getLocation() {
		return location;
	}

	/**
	 * Get the list of dependencies for this module.
	 *
	 * @return
	 */
	public List<SemanticDependency> getDependencies() {
		return Collections.unmodifiableList(dependencies);
	}
}
