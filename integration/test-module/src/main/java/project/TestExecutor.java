/**
 *
 * Copyright to the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package project;

import org.apache.maven.cli.MavenCli;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class TestExecutor {
    public static void execute(File folder) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        PrintStream printStream = new PrintStream(output);
        ProjectExtractor.extract(folder);

        MavenCli cli = new MavenCli();
        int result = cli.doMain(
                new String[]{"test"},
                folder.getAbsolutePath(),
                printStream, printStream);
        printStream.flush();
        System.out.println(getOutput(output));

        assertEquals(getOutput(output), 0, result);
    }

    private static String getOutput(ByteArrayOutputStream output) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n\n==============================\n");
        builder.append("  MAVEN CONSOLE OUTPUT\n");
        builder.append("==============================\n");
        builder.append(new String(output.toByteArray()));
        builder.append("\n==============================\n\n");
        return builder.toString();
    }
}
