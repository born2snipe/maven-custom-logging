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
package com.github.born2snipe.maven.log.agent.support;

import java.util.concurrent.atomic.AtomicBoolean;

public class LineServerManager {
    public static boolean enabled = false;
    protected static int PORT = 36480;
    private static final String PLUGIN_STARTING_PATTERN = ".*?--- .+ ---";
    private static LineServer server;
    private static final AtomicBoolean running = new AtomicBoolean(false);

    public static void manage(String line, final LineListener lineListener) {
        if (isNotRunningAlready() && isSurefirePluginStarting(line)) {
            server = new LineServer(PORT);
            server.addListener(lineListener);
            server.start();
            running.set(true);
        } else if (isRunning() && isBuildFinished(line)) {
            server.stop();
            running.set(false);
            server = null;
        }
    }

    private static boolean isBuildFinished(String line) {
        return line.contains("BUILD FAILURE") || line.contains("BUILD SUCCESS");
    }

    private static boolean isNotRunningAlready() {
        return !isRunning();
    }

    private static boolean isRunning() {
        return running.get();
    }

    private static boolean isSurefirePluginStarting(String line) {
        return isPluginStarting(line) && line.contains("surefire");
    }

    private static boolean isPluginStarting(String line) {
        return line.matches(PLUGIN_STARTING_PATTERN);
    }
}
