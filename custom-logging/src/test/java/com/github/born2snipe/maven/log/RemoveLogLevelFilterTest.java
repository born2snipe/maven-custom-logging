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
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RemoveLogLevelFilterTest {
    private RemoveLogLevelFilter filter;

    @Before
    public void setUp() throws Exception {
        filter = new RemoveLogLevelFilter();
    }

    @Test
    public void shouldNotCareAboutCase() {
        assertEquals("", filter.filter(context("info", true, MockLogLevel.INFO.name())));
    }

    @Test
    public void removeLeadingScopeText() {
        for (MockLogLevel level : MockLogLevel.values()) {
            assertEquals("", filter.filter(context(true, level.name())));
        }
    }

    @Test
    public void doNotThingWithTheFlagIsFalse() {
        for (MockLogLevel level : MockLogLevel.values()) {
            assertEquals("[" + level.name() + "] ", filter.filter(context(false, level.name())));
        }
    }

    private LogEntryFilter.Context context(boolean removeLogLevel, String logLevel) {
        return context(logLevel, removeLogLevel, logLevel);
    }

    private LogEntryFilter.Context context(String text, boolean removeLogLevel, String logLevel) {
        Config config = new Config();
        config.setRemoveLogLevel(removeLogLevel);
        return new LogEntryFilter.Context("[" + text + "] ", config, false, logLevel);
    }
}
