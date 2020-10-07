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

/**
 * Represents a version number with three components: the major component;
 * the minor component; and, the micro component. For example, "1.0.3" is a
 * version number whose major component is "1", whose minor component is "0"
 * and whose micro component is "3".
 *
 * @author David J. Pearce
 *
 */
public final class SemanticVersion implements Comparable<SemanticVersion> {
	/**
	 * The major version number of this plugin. Plugins with the same
	 * identifier and identical major versions may not be backwards
	 * compatible,
	 */
	private int major;

	/**
	 * The minor version number of this plugin. Plugins with the same
	 * identifier and identical major versions should be backwards
	 * compatible,
	 */
	private int minor;

	/**
	 * The micro version number of this plugin. Plugins with the same
	 * identifier and identical major versions should be backwards
	 * compatible,
	 */
	private int micro;

	/**
	 * Construct a version from a string in the format "xxx.yyy.zzz", where
	 * "xxx" is the major number, "yyy" the minor number and "zzz" the micro
	 * number.
	 *
	 * @param versionString
	 */
	public SemanticVersion(String versionString) {
		String[] components = versionString.split("\\.");
		if (components.length != 3) {
			throw new IllegalArgumentException("Invalid version string \""
					+ versionString + "\"");
		}
		this.major = Integer.parseInt(components[0]);
		this.minor = Integer.parseInt(components[1]);
		this.micro = Integer.parseInt(components[2]);
	}

	/**
	 * Get the major version component (e.g. the <code>X</code> in <code>X.Y.Z</code>)
	 *
	 * @return
	 */
	public int getMajor() {
		return major;
	}

	/**
	 * Get the minor version component (e.g. the <code>Y</code> in
	 * <code>X.Y.Z</code>)
	 *
	 * @return
	 */
	public int getMinor() {
		return minor;
	}

	/**
	 * Get the micro or patch version component (e.g. the <code>Z</code> in
	 * <code>X.Y.Z</code>)
	 *
	 * @return
	 */
	public int getMicro() {
		return micro;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof SemanticVersion) {
			SemanticVersion v = (SemanticVersion) o;
			return major == v.major && minor == v.minor && micro == v.micro;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return major ^ minor ^ micro;
	}

	@Override
	public int compareTo(SemanticVersion o) {
		if (major < o.major) {
			return -1;
		} else if (major > o.major) {
			return 1;
		} else if (minor < o.minor) {
			return -1;
		} else if (minor > o.minor) {
			return 1;
		} else if (micro < o.micro) {
			return -1;
		} else if (micro > o.micro) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		return major + "." + minor + "." + micro;
	}
}