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
 * Represents a dependency from one plugin to another.
 *
 * @author David J. Pearce
 *
 */
public class SemanticDependency {
	/**
	 * The unique plugin identifier.
	 */
	private String id;

	/**
	 * The minimum version number permitted, or null if no lower bound.
	 */
	private SemanticVersion minVersion;

	/**
	 * The maximum version number permitted, or null if no upper bound.
	 */
	private SemanticVersion maxVersion;

	public SemanticDependency(String id, SemanticVersion min, SemanticVersion max) {
		this.id = id;
		this.minVersion = min;
		this.maxVersion = max;
	}

	public String getId() {
		return id;
	}

	public boolean matches(String id, SemanticVersion version) {
		return this.id.equals(id)
				&& (this.minVersion == null || this.minVersion
						.compareTo(version) <= 0)
				&& (this.maxVersion == null || this.maxVersion
						.compareTo(version) >= 0);
	}

	@Override
	public String toString() {
		String min = minVersion != null ? minVersion.toString() : "_";
		String max = maxVersion != null ? maxVersion.toString() : "_";
		return id + "[" + min + "," + max + "]";
	}
}