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

import java.util.HashMap;

import wycc.lang.Feature;
import wycc.lang.Module;

public class StdModuleContext implements Module.Context {

	/**
	 * Logging stream, which is null by default.
	 */
	private Logger logger = Logger.NULL;

	/**
	 * The extension points represent registered implementations of interfaces.
	 * Each extension point represents a class that will be instantiated and
	 * configured, and will contribute to some function within the compiler. The
	 * main extension points are: <i>Routes</i>, <i>Builders</i> and
	 * <i>ContentTypes</i>.
	 */
	public final HashMap<Class<?>, Module.ExtensionPoint<?>> extensionPoints = new HashMap<>();

	// ==================================================================
	// Methods
	// ==================================================================

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	@Override
	public <T extends Feature> void register(Class<T> ep, T feature) {
		Module.ExtensionPoint<T> container = (Module.ExtensionPoint<T>) extensionPoints.get(ep);
		if (ep == null) {
			throw new RuntimeException("Missing extension point: " + ep.getCanonicalName());
		} else {
			container.register(feature);
		}
	}

	@Override
	public <T extends Feature> void create(Class<T> extension, Module.ExtensionPoint<T> ep) {
		if (extensionPoints.containsKey(extension)) {
			throw new RuntimeException("Extension point already exists: " + extension);
		} else {
			extensionPoints.put(extension, ep);
		}
	}

	@Override
	public void logTimedMessage(String msg, long time, long memory) {
		logger.logTimedMessage(msg, time, memory);
	}
}
