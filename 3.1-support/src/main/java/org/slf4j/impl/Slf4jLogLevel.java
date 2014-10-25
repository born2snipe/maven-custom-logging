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
package org.slf4j.impl;

import com.github.born2snipe.maven.log.LogLevel;
import org.slf4j.spi.LocationAwareLogger;

public class Slf4jLogLevel implements LogLevel {
    private String text;

    public Slf4jLogLevel(int level) {
        switch (level) {
            case LocationAwareLogger.TRACE_INT:
                text = "TRACE";
                break;
            case LocationAwareLogger.DEBUG_INT:
                text = "DEBUG";
                break;
            case LocationAwareLogger.WARN_INT:
                text = "WARN";
                break;
            case LocationAwareLogger.ERROR_INT:
                text = "ERROR";
                break;
            case LocationAwareLogger.INFO_INT:
                text = "INFO";
                break;
            default:
                throw new IllegalArgumentException("Not sure what log level this is (level=" + level + ")");
        }
    }

    @Override
    public String text() {
        return text;
    }
}
