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
        assertEquals("", filter.filter(context(Level.DEBUG, "debug", true)));
    }

    @Test
    public void removeLeadingScopeText() {
        for (Level level : Level.values()) {
            assertEquals("", filter.filter(context(level, true)));
        }
    }

    @Test
    public void doNotThingWithTheFlagIsFalse() {
        for (Level level : Level.values()) {
            assertEquals("[" + level.text + "] ", filter.filter(context(level, false)));
        }
    }

    private LogEntryFilter.Context context(Level level, boolean removeLogLevel) {
        return context(level, level.text, removeLogLevel);
    }

    private LogEntryFilter.Context context(Level level, String text, boolean removeLogLevel) {
        Config config = new Config();
        config.setRemoveLogLevel(removeLogLevel);
        return new LogEntryFilter.Context(level, "[" + text + "] ", config);
    }
}
