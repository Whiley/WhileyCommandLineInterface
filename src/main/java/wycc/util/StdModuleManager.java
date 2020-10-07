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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import wybs.util.Logger;
import wycc.lang.Descriptor;
import wycc.lang.Module;
import wycc.lang.SemanticDependency;
import wycc.lang.SemanticVersion;


public class StdModuleManager {

	/**
	 * Logging stream, which is null by default.
	 */
	private Logger logger = Logger.NULL;

	/**
	 * The list of activated modules
	 */
	private ArrayList<Descriptor> modules = new ArrayList<>();

	private HashMap<Class<? extends Module>,Module> instances = new HashMap<>();

	/**
	 * The module context used to manage extension points for modules.
	 *
	 * @param locations
	 */
	private Module.Context context;

	public StdModuleManager(Module.Context context,
			List<Descriptor> modules) {
		this.modules = new ArrayList<>(modules);
		this.context = context;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 * Get instance of given module within this context, or null if no
	 * instance available.
	 *
	 * @param module
	 * @return
	 */
	public <T extends Module> T getInstance(Class<T> module) {
		return (T) instances.get(module);
	}

	/**
	 * Scan and activate all modules on the search path. As part of this, all
	 * module dependencies will be checked.
	 */
	public void start() {

		// Construct the URLClassLoader which will be used to load
		// classes within the modules.
		URL[] urls = new URL[modules.size()];
		for(int i=0;i!=modules.size();++i) {
			urls[i] = modules.get(i).getLocation();
		}
		URLClassLoader loader = new URLClassLoader(urls);

		// Third, active the modules. This will give them the opportunity to
		// register whatever extensions they like.
		activateModules(loader);
	}

	/**
	 * Deactivate all modules previously activated.
	 */
	public void stop() {
		deactiveModules();
	}

	/**
	 * Activate all modules in the order of occurrence in the given list. It is
	 * assumed that all dependencies are already resolved prior to this and all
	 * modules are topologically sorted.
	 */
	private void activateModules(URLClassLoader loader) {
		for (int i = 0; i != modules.size(); ++i) {
			Descriptor module = modules.get(i);
			try {
				Class c = loader.loadClass(module.getActivator());
				Module.Activator self = (Module.Activator) c.newInstance();
				Module instance = self.start(context);
				instances.put(c, instance);
				logger.logTimedMessage("Activated module " + module.getId() + " (v" + module.getVersion() + ")", 0, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Deactivate all modules in the reverse order of occurrence in the given
	 * list. It is assumed that all dependencies are already resolved prior to
	 * this and all modules are topologically sorted.
	 */
	private void deactiveModules() {

		// TODO!

	}
}
