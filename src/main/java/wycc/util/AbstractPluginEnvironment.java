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

import java.util.List;
import java.util.concurrent.ExecutorService;

import wybs.util.AbstractCompilationUnit.Value.UTF8;
import wycc.cfg.Configuration;
import wycc.lang.Command;
import wycc.lang.Module;
import wyfs.lang.Content;
import wyfs.lang.Path;
import wyfs.util.Trie;

public abstract class AbstractPluginEnvironment extends AbstractCommandEnvironment {

	/**
	 * Provides the default plugin context.
	 */
	private final StdModuleContext context = new StdModuleContext();

	public AbstractPluginEnvironment(Configuration configuration, Logger logger, ExecutorService executor) {
		super(configuration, logger, executor);
		createTemplateExtensionPoint();
		createContentTypeExtensionPoint();
		createBuildPlatformExtensionPoint();
		activateDefaultPlugins(configuration);
	}

	/**
	 * Create the Build.Template extension point. This is where plugins register
	 * their primary functionality for constructing a specific build project.
	 *
	 * @param context
	 * @param templates
	 */
	private void createTemplateExtensionPoint() {
		context.create(Command.Descriptor.class, new Module.ExtensionPoint<Command.Descriptor>() {
			@Override
			public void register(Command.Descriptor command) {
				commandDescriptors.add(command);
			}
		});
	}

	/**
	 * Create the Content.Type extension point.
	 *
	 * @param context
	 * @param templates
	 */
	private void createContentTypeExtensionPoint() {
		context.create(Content.Type.class, new Module.ExtensionPoint<Content.Type>() {
			@Override
			public void register(Content.Type contentType) {
				contentTypes.add(contentType);
			}
		});
	}


	/**
	 * Create the Content.Type extension point.
	 *
	 * @param context
	 * @param templates
	 */
	private void createBuildPlatformExtensionPoint() {
		context.create(wybs.lang.Build.Platform.class, new Module.ExtensionPoint<wybs.lang.Build.Platform>() {
			@Override
			public void register(wybs.lang.Build.Platform platform) {
				buildPlatforms.add(platform);
			}
		});
	}
	/**
	 * Activate the default set of plugins which the tool uses. Currently this list
	 * is statically determined, but eventually it will be possible to dynamically
	 * add plugins to the system.
	 *
	 * @param verbose
	 * @param locations
	 * @return
	 */
	private void activateDefaultPlugins(Configuration global) {
		// Determine the set of install plugins
		List<Path.ID> plugins = global.matchAll(Trie.fromString("plugins/*"));
		// start modules
		for (Path.ID id : plugins) {
			UTF8 activator = global.get(UTF8.class, id);
			try {
				Class<?> c = Class.forName(activator.toString());
				Module.Activator instance = (Module.Activator) c.newInstance();
				instance.start(context);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

}
