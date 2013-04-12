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

package org.apache.maven.log;

public enum Level {
    DEBUG("DEBUG"),
    INFO("INFO"),
    WARN("WARNING"),
    ERROR("ERROR"),
    FATAL("FATAL"),
    DISABLED("");

    public final String text;

    private Level(String text) {
        this.text = text;
    }

    public static Level valueOf(int level) {
        return values()[level];
    }

    public static Level valueFromLogText(String logText) {
        for (Level level : values()) {
            if (logText.startsWith("[" + level.text + "]"))
                return level;
        }
        return null;
    }
}
