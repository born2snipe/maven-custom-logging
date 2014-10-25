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

import com.github.born2snipe.maven.log.LogEntryFilter;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = LogEntryFilter.class, position = Integer.MIN_VALUE)
public class ClearLineFilter implements LogEntryFilter {
    public static boolean clearLine = false;

    @Override
    public String filter(Context context) {
        if (clearLine) {
            return "";
        }
        return context.entryText;
    }
}
