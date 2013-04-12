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

package org.apache.maven.cli;

import org.apache.maven.Maven;
import org.apache.maven.log.Config;
import org.apache.maven.log.ConfigSerializer;
import org.apache.maven.log.Level;
import org.apache.maven.log.LogEntryFilter;
import org.codehaus.plexus.logging.AbstractLogger;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.StringUtils;
import org.openide.util.Lookup;

import java.io.PrintStream;
import java.util.Collection;

public class PrintStreamLogger extends AbstractLogger {

    private Collection<? extends LogEntryFilter> logEntryFilters;

    static interface Provider {
        PrintStream getStream();
    }

    private Provider provider;
    private Config config;

    public static final String FATAL_ERROR = "[FATAL] ";
    public static final String ERROR = "[ERROR] ";
    public static final String WARNING = "[WARNING] ";
    public static final String INFO = "[INFO] ";
    public static final String DEBUG = "[DEBUG] ";

    public PrintStreamLogger(Provider provider) {
        super(Logger.LEVEL_INFO, Maven.class.getName());

        if (provider == null) {
            throw new IllegalArgumentException("output stream provider missing");
        }
        this.provider = provider;

        config = new ConfigSerializer().load("config/default.yml");
        logEntryFilters = Lookup.getDefault().lookupAll(LogEntryFilter.class);
    }


    public PrintStreamLogger(PrintStream out) {
        super(Logger.LEVEL_INFO, Maven.class.getName());

        setStream(out);
    }

    public void setStream(final PrintStream out) {
        if (out == null) {
            throw new IllegalArgumentException("output stream missing");
        }

        this.provider = new Provider() {
            public PrintStream getStream() {
                return out;
            }
        };
    }

    public void debug(String message, Throwable throwable) {
        if (isDebugEnabled()) {
            log(message, throwable, DEBUG);
        }
    }

    public void info(String message, Throwable throwable) {
        if (isInfoEnabled()) {
            log(message, throwable, INFO);
        }
    }

    public void warn(String message, Throwable throwable) {
        if (isWarnEnabled()) {
            log(message, throwable, WARNING);
        }
    }

    public void error(String message, Throwable throwable) {
        if (isErrorEnabled()) {
            log(message, throwable, ERROR);
        }
    }

    public void fatalError(String message, Throwable throwable) {
        if (isFatalErrorEnabled()) {
            log(message, throwable, FATAL_ERROR);
        }
    }

    private void log(String message, Throwable throwable, String levelName) {
        PrintStream out = provider.getStream();

        Level level = Level.valueOf(getThreshold());
        String textToBeLogged = levelName + message;
        for (LogEntryFilter logEntryFilter : logEntryFilters) {
            textToBeLogged = logEntryFilter.filter(new LogEntryFilter.Context(level, textToBeLogged, config));
            if (StringUtils.isBlank(textToBeLogged)) {
                break;
            }
        }

        if (StringUtils.isNotBlank(textToBeLogged)) {
            out.println(textToBeLogged);
        }

        if (null != throwable) {
            throwable.printStackTrace(out);
        }
    }

    public void close() {
        PrintStream out = provider.getStream();

        if (out == System.out || out == System.err) {
            out.flush();
        } else {
            out.close();
        }
    }

    public Logger getChildLogger(String arg0) {
        return this;
    }
}