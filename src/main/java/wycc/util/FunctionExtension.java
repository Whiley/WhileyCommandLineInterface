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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import wycc.lang.Feature;
import wycc.lang.Module;

public class FunctionExtension implements Feature {
	private Module receiver;
	private Method method;

	public FunctionExtension(Module receiver, String name, java.lang.Class... parameters) {
		this.receiver = receiver;
		try {
			this.method = receiver.getClass().getMethod(name, parameters);
		} catch (Exception e) {
			throw new RuntimeException("No such method: " + name, e);
		}
	}

	private static final ArrayList<FunctionExtension> functions = new ArrayList<>();

	public static void register(FunctionExtension fe) {
		functions.add(fe);
	}

	/**
	 *
	 * @param functions
	 * @param target
	 * @param outputDirectory
	 * @param libraries
	 */
	public static Object invoke(String name, Object... arguments) {
		FunctionExtension fn = null;
		for (FunctionExtension f : functions) {
			if (f.method.getName().equals(name)) {
				fn = f;
				break;
			}
		}
		if (fn != null) {
			try {
				return fn.method.invoke(fn.receiver, arguments);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Error: unable to find method \"" + name + "\"");
		}
		return null;
	}
}
