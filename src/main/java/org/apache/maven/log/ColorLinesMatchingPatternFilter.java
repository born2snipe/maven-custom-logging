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

import org.apache.maven.log.config.LinePatternColoringConfig;
import org.codehaus.plexus.util.StringUtils;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.openide.util.lookup.ServiceProvider;

import java.lang.reflect.Field;

import static org.fusesource.jansi.Ansi.ansi;

@ServiceProvider(service = LogEntryFilter.class, position = Integer.MAX_VALUE)
public class ColorLinesMatchingPatternFilter implements LogEntryFilter {
    public ColorLinesMatchingPatternFilter() {
        AnsiConsole.systemInstall();
    }

    @Override
    public String filter(Context context) {
        for (LinePatternColoringConfig coloring : context.config.getColoring()) {
            if (isMatch(context, coloring)) {
                if (StringUtils.isNotBlank(coloring.getRender())) {
                    return ansi().render(
                            context.entryText.replaceAll(coloring.getPattern(), coloring.getRender())
                    ).toString();
                } else {
                    throw new IllegalArgumentException("Please provide a 'render' value for your pattern of [" + coloring.getPattern() + "]");
                }
            }
        }
        return context.entryText;
    }

    private boolean isMatch(Context context, LinePatternColoringConfig coloring) {
        return context.entryText.matches(coloring.getPattern());
    }

    private Ansi.Color getColorOf(String colorName) {
        try {
            if (colorName == null) {
                return null;
            }
            Field field = Ansi.Color.class.getField(colorName.toUpperCase());
            field.setAccessible(true);
            return (Ansi.Color) field.get(Ansi.Color.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
