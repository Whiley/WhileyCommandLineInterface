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

import java.util.Arrays;
import java.util.Collection;

/**
 * <p>
 * Represents a registered component provided by a given module. The
 * intuition is that a feature provides some functionality which could be
 * configured, etc. Features essentially form an object hierarchy within the
 * module system, and provide systematic way for users to configure a module
 * system.
 * </p>
 *
 * @author David J. Pearce
 *
 */
public interface Feature {

	/**
	 * A configurable feature is one which has settings that may be configured
	 * dynamically. Not all features are configurable (though most are).
	 *
	 * @author David J. Pearce
	 *
	 */
	public interface Configurable extends Feature {

		/**
		 * Get the list of configurable options.
		 *
		 * @return
		 */
		public String[] getOptions();

		/**
		 * Get a description of a particular configurable option
		 *
		 * @return
		 */
		public String describe(String name);

		/**
		 * Set a given attribute to a given value in this configuration.
		 *
		 * @param name
		 *            Name of the attribute in question
		 * @param value
		 *            Data to which the attribute is set
		 */
		public void set(String name, Object value) throws ConfigurationError;

		/**
		 * Get the value assigned to a given attribute in this configuration.
		 *
		 * @param name
		 *            Name of the attribute in question
		 * @param value
		 *            Data to which the attribute is set
		 */
		public Object get(String name);
	}

	/**
	 * A configuration error can occur when setting a configuration option on a
	 * given feature. This could happen, for example, if the feature does not
	 * support the given option, etc.
	 *
	 * @author David J. Pearce
	 *
	 */
	public class ConfigurationError extends Exception {
		public ConfigurationError(String message) {
			super(message);
		}
		public ConfigurationError(String message, Throwable t) {
			super(message,t);
		}
		public ConfigurationError(Throwable t) {
			super(t);
		}
	}
}
