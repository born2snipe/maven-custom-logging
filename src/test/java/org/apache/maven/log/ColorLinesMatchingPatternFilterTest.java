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

import org.apache.maven.log.config.Config;
import org.apache.maven.log.config.LinePatternColoringConfig;
import org.junit.Before;
import org.junit.Test;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;
import static org.junit.Assert.assertEquals;

public class ColorLinesMatchingPatternFilterTest {
    private ColorLinesMatchingPatternFilter filter;

    @Before
    public void setUp() throws Exception {
        filter = new ColorLinesMatchingPatternFilter();
    }

    @Test(expected = RuntimeException.class)
    public void blowUpIfNoRenderValueIsProvided() {
        Config config = config(colorConfig("hello", ""));

        filter.filter(new LogEntryFilter.Context(null, "hello", config));
    }

    @Test
    public void multipleColorConfigs() {
        Config config = config(colorConfig("hello", "bg_red,fg_white"), colorConfig("world", "@|bg_green,fg_blue $0|@"));

        String result = filter.filter(new LogEntryFilter.Context(null, "world", config));
        assertEquals(ansi().bg(GREEN).fg(BLUE).a("world").reset().toString(), result);

    }

    @Test
    public void lineDoesNotMatch() {
        Config config = config(colorConfig("hello", "@|bg_red,fg_white $0|@"));

        assertEquals("world", filter.filter(new LogEntryFilter.Context(null, "world", config)));
    }

    @Test(expected = RuntimeException.class)
    public void blowUpForUnknownColor() {
        Config config = config(colorConfig("hello", "@|unknown $0|@"));

        filter.filter(new LogEntryFilter.Context(null, "hello", config));
    }

    @Test
    public void noForegroundColor() {
        Config config = config(colorConfig("hello", "@|bg_red $0|@"));

        String result = filter.filter(new LogEntryFilter.Context(null, "hello", config));

        assertEquals(ansi().bg(RED).a("hello").reset().toString(), result);
    }

    @Test
    public void noBackgroundColor() {
        Config config = config(colorConfig("hello", "@|white $0|@"));

        String result = filter.filter(new LogEntryFilter.Context(null, "hello", config));

        assertEquals(ansi().fg(WHITE).a("hello").reset().toString(), result);
    }

    @Test
    public void lineMatchesWithBackgroundAndForegroundColor() {
        Config config = config(colorConfig("hello", "@|bg_red,fg_white $0|@"));

        String result = filter.filter(new LogEntryFilter.Context(null, "hello", config));

        assertEquals(ansi().bg(RED).fg(WHITE).a("hello").reset().toString(), result);
    }

    private Config config(LinePatternColoringConfig... colorConfigs) {
        Config config = new Config();
        config.setColoring(colorConfigs);
        return config;
    }

    private LinePatternColoringConfig colorConfig(String pattern, String render) {
        LinePatternColoringConfig coloringConfig = new LinePatternColoringConfig();
        coloringConfig.setPattern(pattern);
        coloringConfig.setRender(render);
        return coloringConfig;
    }
}
