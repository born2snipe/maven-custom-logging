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

import org.apache.commons.lang.StringUtils;
import org.openide.util.lookup.ServiceProvider;

import java.text.SimpleDateFormat;
import java.util.Date;

@ServiceProvider(service = LogEntryFilter.class, position = 0)
public class AddTimestampFilter implements LogEntryFilter {

    @Override
    public String filter(Context context) {
        StringBuilder builder = new StringBuilder();
        if (isPatternProvided(context)) {
            builder.append(formatter(context).format(new Date()));
            builder.append(" ");
        }
        builder.append(context.entryText);
        return builder.toString();
    }

    private boolean isPatternProvided(Context context) {
        return StringUtils.isNotBlank(context.config.getTimestampPattern());
    }

    private SimpleDateFormat formatter(Context context) {
        return new SimpleDateFormat(context.config.getTimestampPattern());
    }
}
