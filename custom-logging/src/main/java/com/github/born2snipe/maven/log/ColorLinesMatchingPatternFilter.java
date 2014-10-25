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

import com.github.born2snipe.maven.log.config.LinePatternColoringConfig;
import org.fusesource.jansi.AnsiConsole;
import org.openide.util.lookup.ServiceProvider;

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
                String result = ansi().render(
                        context.entryText.replaceAll(coloring.getPattern(), coloring.getRender())
                ).toString();

                if (context.debug) {
                    logDebugInfo(context, coloring, result);
                }

                return result;
            }
        }
        return context.entryText;
    }

    private void logDebugInfo(Context context, LinePatternColoringConfig coloring, String result) {
        System.out.println("===============================");
        System.out.println("pattern: [" + coloring.getPattern() + "]");
        System.out.println("render: [" + coloring.getRender() + "]");
        System.out.println("before: [" + context.entryText + "]");
        System.out.println("after: [" + result + "]");
        System.out.println("===============================");
    }

    private boolean isMatch(Context context, LinePatternColoringConfig coloring) {
        return context.entryText.matches(coloring.getPattern());
    }

}
