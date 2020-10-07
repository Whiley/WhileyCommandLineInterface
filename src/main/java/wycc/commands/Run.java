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
package wycc.commands;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import wybs.lang.Build;
import wybs.util.Logger;
import wybs.util.AbstractCompilationUnit.Value;
import wycc.cfg.Configuration;
import wycc.cfg.Configuration.Schema;
import wycc.lang.Command;
import wyfs.lang.Path;
import wyfs.util.Trie;

public class Run implements Command {
	public static final Trie BUILD_MAIN = Trie.fromString("build/main");

	/**
	 * The descriptor for this command.
	 */
	public static final Command.Descriptor DESCRIPTOR = new Command.Descriptor() {
		@Override
		public String getName() {
			return "run";
		}

		@Override
		public String getDescription() {
			return "Execute method in package";
		}

		@Override
		public List<Option.Descriptor> getOptionDescriptors() {
			return Collections.EMPTY_LIST;
		}

		@Override
		public Schema getConfigurationSchema() {
			return Configuration.EMPTY_SCHEMA;
		}

		@Override
		public List<Descriptor> getCommands() {
			return Collections.EMPTY_LIST;
		}

		@Override
		public Command initialise(Command.Environment environment) {
			return new Run(environment, System.out, System.err);
		}

	};

	/**
	 * Provides a generic place to which normal output should be directed. This
	 * should eventually be replaced.
	 */
	private final PrintStream sysout;

	/**
	 * Provides a generic place to which error output should be directed. This
	 * should eventually be replaced.
	 */
	private final PrintStream syserr;

	/**
	 * The enclosing environment for this command.
	 */
	private final Command.Environment environment;

	private final Value.UTF8 method;

	public Run(Command.Environment environment, OutputStream sysout,
			OutputStream syserr) {
		this.environment = environment;
		this.sysout = new PrintStream(sysout);
		this.syserr = new PrintStream(syserr);
		//
		if(environment.hasKey(BUILD_MAIN)) {
			this.method = environment.get(Value.UTF8.class, BUILD_MAIN);
		} else {
			this.method = null;
		}
	}

	@Override
	public Descriptor getDescriptor() {
		return DESCRIPTOR;
	}

	@Override
	public void initialise() {

	}

	@Override
	public void finalise() {

	}

	@Override
	public boolean execute(Command.Project project, Template template) {
		throw new IllegalArgumentException("implement me");
	}
}
