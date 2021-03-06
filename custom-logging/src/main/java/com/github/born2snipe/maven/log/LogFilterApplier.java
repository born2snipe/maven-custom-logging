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

package com.github.born2snipe.maven.log;

import com.github.born2snipe.maven.log.config.Config;
import com.github.born2snipe.maven.log.config.ConfigLoader;
import org.apache.commons.lang.StringUtils;
import org.openide.util.Lookup;

import java.util.Collection;

public class LogFilterApplier {
    public static final String OFF_SWITCH = "custom.logging.off";
    public static final String DEBUG_SWITCH = "custom.logging.debug";
    private ConfigLoader configLoader = new ConfigLoader();
    private Collection<? extends LogEntryFilter> filters;
    private static Config config;

    static void reset() {
       config = null;
    }

    public LogFilterApplier() {
        filters = Lookup.getDefault().lookupAll(LogEntryFilter.class);
    }

    public String apply(String text, String logLevel) {
        if (System.getProperties().containsKey(OFF_SWITCH)) {
            return text;
        }

        boolean displayDebugInfo = showDebugInfo();

        if (config == null) config = configLoader.loadConfiguration(displayDebugInfo);

        String result = text;
        if (displayDebugInfo) System.out.println("Original log Text: [" + text + "]");

        for (LogEntryFilter filter : filters) {
            LogEntryFilter.Context context = new LogEntryFilter.Context(
                    result, config, displayDebugInfo, logLevel
            );
            result = filter.filter(context);

            if (displayDebugInfo) System.out.println("Log text filtered by " + filter + ": [" + result + "]");

            if (StringUtils.isBlank(result)) {
                return result;
            }
        }

        return result;
    }

    public boolean showDebugInfo() {
        return System.getProperties().containsKey(DEBUG_SWITCH);
    }

    public void setConfigLoader(ConfigLoader configLoader) {
        this.configLoader = configLoader;
    }
}
