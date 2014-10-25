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

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class AddTimestampFilterTest {
    private AddTimestampFilter filter;

    @Before
    public void setUp() throws Exception {
        filter = new AddTimestampFilter();
    }

    @Test
    public void patternProvided() {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        assertEquals(format.format(new Date()) + " entry", filter.filter(context("entry", "MM/dd/yyyy")));
    }

    @Test
    public void noDatePatternProvided() {
        assertEquals("entry", filter.filter(context("entry", "  ")));
    }

    private LogEntryFilter.Context context(String entry, String datePattern) {
        Config config = new Config();
        config.setTimestampPattern(datePattern);
        return new LogEntryFilter.Context(null, entry, config, false);
    }
}
