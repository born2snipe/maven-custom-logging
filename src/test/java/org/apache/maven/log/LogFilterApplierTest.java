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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static org.apache.maven.log.LogFilterApplier.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LogFilterApplierTest {
    @Mock
    private EnvAccessor envAccessor;
    @Mock
    private ConfigSerializer serializer;
    @InjectMocks
    private LogFilterApplier applier;
    private Config config;

    @Before
    public void setUp() throws Exception {
        ClearLineFilter.clearLine = false;
        config = new Config();

        System.getProperties().remove(OFF_SWITCH);
        expectSystemPropertyConfig(null);
        expectGlobalConfig(null);
        when(serializer.load("config/default.yml")).thenReturn(config);
    }

    @Test
    public void systemPropertySwitchGivenToShutOffFiltering() {
        System.setProperty(OFF_SWITCH, "");
        config.setRemoveLogLevel(true);

        assertEquals("[INFO] text", applier.apply("[INFO] text", Level.INFO));
    }

    @Test
    public void shouldNotAttemptToLoadSystemPropertyConfigFileWhenNoValueIsGiven() {
        expectSystemPropertyConfig(null);

        applier.apply("[INFO] test", Level.INFO);

        verify(serializer, never()).load(new File(""));
    }

    @Test
    public void envVariableConfigFileLocation() {
        config.setRemoveLogLevel(true);

        expectEnvConfig(config);
        expectGlobalConfig(new Config());

        assertEquals("test", applier.apply("[INFO] test", Level.INFO));
    }

    @Test
    public void systemPropertyLocationOfConfigFileShouldTrumpEverything() {
        Config systemPropertyConfig = new Config();
        systemPropertyConfig.setRemoveLogLevel(true);

        expectSystemPropertyConfig(systemPropertyConfig);
        expectGlobalConfig(new Config());
        expectEnvConfig(new Config());

        assertEquals("test", applier.apply("[INFO] test", Level.INFO));
    }

    @Test
    public void shouldAttemptToFindAGlobalConfigFileInTheClassPathBeforeUsingTheDefault() {
        Config globalConfig = new Config();
        globalConfig.setRemoveLogLevel(true);
        expectGlobalConfig(globalConfig);

        assertEquals("test", applier.apply("[INFO] test", Level.INFO));
    }

    @Test
    public void shouldNotAddTimestampToLineThatHasBeenClearedByAnotherFilter() {
        ClearLineFilter.clearLine = true;
        config.setTimestampPattern("yyyy/MM/dd");

        assertEquals("", applier.apply("[INFO] test", Level.INFO));
    }

    @Test
    public void shouldNotAddTimestampToBlankLines() {
        config.setTimestampPattern("yyyy/MM/dd");

        assertEquals("", applier.apply("", Level.INFO));
    }

    @Test
    public void configuredAddTimestampAndRemoveLogLevel() {
        config.setRemoveLogLevel(true);
        config.setTimestampPattern("yyyy/MM/dd");

        assertTrue(applier.apply("[INFO] text", Level.INFO).matches("[0-9]{4}/[0-9]{2}/[0-9]{2} text"));
    }

    @Test
    public void configuredToRemoveLogLevel() {
        config.setRemoveLogLevel(true);

        assertEquals("text", applier.apply("[INFO] text", Level.INFO));
    }

    @Test
    public void nothingIsConfigured() {
        assertEquals("[INFO] text", applier.apply("[INFO] text", Level.INFO));
    }

    private void expectSystemPropertyConfig(Config config) {
        if (config == null) {
            System.setProperty(CONFIG_SYSTEM_PROPERTY, "");
        } else {
            System.setProperty(CONFIG_SYSTEM_PROPERTY, "config-location");
            File configFile = new File("config-location");
            when(serializer.load(configFile)).thenReturn(config);
        }
    }

    private void expectGlobalConfig(Config config) {
        File configFile = new File(MavenCli.DEFAULT_GLOBAL_SETTINGS_FILE.getParentFile(), GLOBAL_CONFIG_NAME);
        when(serializer.quietLoad(configFile)).thenReturn(config);
    }

    private void expectEnvConfig(Config config) {
        when(envAccessor.get(LogFilterApplier.ENV_CONFIG_LOCATION)).thenReturn("env_config");
        when(serializer.quietLoad(new File("env_config"))).thenReturn(config);
    }
}
