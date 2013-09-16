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

import org.codehaus.plexus.logging.Logger;
import org.junit.Test;

import java.lang.reflect.Field;

import static junit.framework.Assert.assertEquals;

public class LevelTest {
    @Test
    public void levelOrdinalShouldMatchLoggerConstantsValues() {
        for (Level level : Level.values()) {
            int expectedValue = getContantValueOf(level);
            assertEquals(level + "'s ordinal is wrong", expectedValue, level.ordinal());
        }
    }

    private int getContantValueOf(Level level) {
        try {
            Field field = Logger.class.getField("LEVEL_" + level.name());
            field.setAccessible(true);
            return (Integer) field.get(Logger.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
