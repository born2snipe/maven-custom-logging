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

import org.apache.maven.cli.MavenCli;
import org.apache.maven.log.config.Config;
import org.apache.maven.log.config.ConfigSerializer;
import org.codehaus.plexus.util.StringUtils;
import org.openide.util.Lookup;

import java.io.File;
import java.util.Collection;

public class LogFilterApplier {
    public static final String CONFIG_SYSTEM_PROPERTY = "custom.logging.configuration";
    public static final String GLOBAL_CONFIG_NAME = "custom-logging.yml";
    public static final String OFF_SWITCH = "custom.logging.off";
    public static final String ENV_CONFIG_LOCATION = "MAVEN_CUSTOM_LOGGING_CONFIG";
    private Collection<? extends LogEntryFilter> filters;
    private Config config;
    private ConfigSerializer configSerializer = new ConfigSerializer();
    private EnvAccessor envAccessor = new EnvAccessor();

    public LogFilterApplier() {
        filters = Lookup.getDefault().lookupAll(LogEntryFilter.class);
    }

    public String apply(String text, Level level) {
        if (System.getProperties().containsKey(OFF_SWITCH)) {
            return text;
        }

        loadConfiguration(level);

        String result = text;
        if (level == Level.DEBUG) System.out.println("Original log Text: [" + text + "]");

        for (LogEntryFilter filter : filters) {
            LogEntryFilter.Context context = new LogEntryFilter.Context(
                    level, result, config
            );
            result = filter.filter(context);

            if (level == Level.DEBUG) System.out.println("Log text filtered by " + filter + ": [" + result + "]");

            if (StringUtils.isBlank(result)) {
                return result;
            }
        }

        return result;
    }

    private void loadConfiguration(Level level) {
        if (config == null) {
            loadSystemPropertyConfigFile(level);
            loadEnvPropertyConfigFile(level);
            loadGlobalConfigFile(level);
            loadDefaultConfigFile(level);
        }
    }

    private void loadEnvPropertyConfigFile(Level level) {
        String value = envAccessor.get(ENV_CONFIG_LOCATION);
        if (config == null && StringUtils.isNotBlank(value)) {
            config = configSerializer.quietLoad(new File(value));
        }
    }

    private void loadSystemPropertyConfigFile(Level level) {
        String configFileLocation = System.getProperty(CONFIG_SYSTEM_PROPERTY);
        if (StringUtils.isNotBlank(configFileLocation)) {
            File configFile = new File(configFileLocation);
            if (level == Level.DEBUG) System.out.println("System property config file: " + configFile);
            config = configSerializer.load(configFile);
        }
    }

    private void loadDefaultConfigFile(Level level) {
        if (config == null) {
            if (level == Level.DEBUG) System.out.println("Using default config file");
            config = configSerializer.load("config/default.yml");
        }
    }

    private void loadGlobalConfigFile(Level level) {
        if (config == null) {
            File configFile = new File(MavenCli.DEFAULT_GLOBAL_SETTINGS_FILE.getParentFile(), GLOBAL_CONFIG_NAME);
            if (level == Level.DEBUG) System.out.println("Global config file: " + configFile);
            config = configSerializer.quietLoad(configFile);
        }
    }

    public void setConfigSerializer(ConfigSerializer configSerializer) {
        this.configSerializer = configSerializer;
    }
}
