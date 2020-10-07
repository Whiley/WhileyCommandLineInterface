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

import wybs.util.Logger;

/**
 * <p>
 * A module describes a collection of one or more features which can be deployed
 * within a running system (for example, though not exclusively, the Whiley
 * Compiler Collection).
 * </p>
 *
 * @author David J. Pearce
 *
 */
public interface Module {

	/**
	 * A module Context provides a mechanism for modules to interact with their
	 * environment. In particular, it allows them to register extension points
	 * which provide the critical mechanism for adding new functionality.
	 *
	 * @author David J. Pearce
	 *
	 */
	public interface Context extends Logger {

		/**
		 * Responsible for registering a feature as implementing an extension
		 * within the system.
		 *
		 * @param ep
		 *            The class representing the extension point (e.g.
		 *            "wyfs.ContentType").
		 * @param extension
		 *            The implementation of the given extension point.
		 */
		public <T extends Feature> void register(Class<T> ep, T extension);

		/**
		 * Create a new extension point which subsequent modules can register
		 * extensions for.
		 *
		 * @param extension
		 * @param ep
		 */
		public <T extends Feature> void create(Class<T> extension, ExtensionPoint<T> ep);
	}

	/**
	 * An extension point in the module is a named entity provided by one
	 * module, which other modules can register extensions for.
	 *
	 * @author David J. Pearce
	 *
	 */
	public interface ExtensionPoint<T extends Feature> {

		/**
		 * Notify extension point that a new extension has been registered for
		 * it.
		 *
		 * @param extension
		 *            The extension implementation to register with this
		 *            extension point.
		 */
		public void register(T feature);
	}

	/**
	 * Represents a class designated as the unique "activator" for a given
	 * module. This activator is used to control aspects of the module (e.g.
	 * resources allocated) as it is started and stopped,
	 *
	 * @author David J. Pearce
	 *
	 */
	public interface Activator {

		/**
		 * This method is called when the module is begun. This gives the module
		 * an opportunity to register one or more extension points in the
		 * compiler.
		 *
		 * @param context
		 */
		public Module start(Context context);

		/**
		 * This method is called when the module is stopped. Any resources used
		 * by the module should be freed at this point. This includes any
		 * registered extension points, which should be unregistered.
		 *
		 * @param context
		 */
		public void stop(Module module, Context context);
	}
}
