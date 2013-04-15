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

package org.apache.maven.log.config;

import org.apache.maven.cli.MavenCli;
import org.apache.maven.log.EnvAccessor;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;

public class ConfigLoader {
    public static final String CONFIG_SYSTEM_PROPERTY = "custom.logging.configuration";
    public static final String CONFIG_FILENAME = "maven-custom-logging.yml";
    public static final String ENV_CONFIG_LOCATION = "MAVEN_CUSTOM_LOGGING_CONFIG";
    public ConfigSerializer configSerializer;
    public EnvAccessor envAccessor;

    public ConfigLoader() {
        this(new ConfigSerializer(), new EnvAccessor());
    }

    public ConfigLoader(ConfigSerializer configSerializer, EnvAccessor envAccessor) {
        this.configSerializer = configSerializer;
        this.envAccessor = envAccessor;
    }

    public Config loadConfiguration(boolean debug) {
        Config config = loadSystemPropertyConfigFile(debug);
        if (config == null) config = loadEnvPropertyConfigFile(debug);
        if (config == null) config = loadUserHomeConfigFile(debug);
        if (config == null) config = loadGlobalConfigFile(debug);
        if (config == null) config = loadDefaultConfigFile(debug);
        return config;
    }

    private Config loadUserHomeConfigFile(boolean debug) {
        File file = new File(System.getProperty("user.home"), CONFIG_FILENAME);
        Config config = configSerializer.quietLoad(file);
        if (config != null && debug) System.out.println("Using config from 'user.home': " + file);
        return config;
    }

    public Config loadEnvPropertyConfigFile(boolean debug) {
        String value = envAccessor.get(ENV_CONFIG_LOCATION);
        if (StringUtils.isNotBlank(value)) {
            Config config = configSerializer.quietLoad(new File(value));
            if (config != null && debug) System.out.println("Using config from $" + ENV_CONFIG_LOCATION + "=" + value);
            return config;
        }
        return null;
    }

    public Config loadSystemPropertyConfigFile(boolean debug) {
        String configFileLocation = System.getProperty(CONFIG_SYSTEM_PROPERTY);
        if (StringUtils.isNotBlank(configFileLocation)) {
            File configFile = new File(configFileLocation);
            if (debug) System.out.println("System property config file: " + configFile);
            return configSerializer.load(configFile);
        }
        return null;
    }

    public Config loadDefaultConfigFile(boolean debug) {
        if (debug) System.out.println("Using default config file");
        return configSerializer.load("config/default.yml");
    }

    public Config loadGlobalConfigFile(boolean debug) {
        File configFile = new File(MavenCli.DEFAULT_GLOBAL_SETTINGS_FILE.getParentFile(), CONFIG_FILENAME);
        Config config = configSerializer.quietLoad(configFile);
        if (config != null && debug) System.out.println("Global config file: " + configFile);
        return config;
    }

}