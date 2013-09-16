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

package org.apache.maven.log.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class ConfigSerializer {
    public Config load(String classPathPath) {
        return loadUrl(classLoader().getResource(classPathPath));
    }

    public Config quietLoad(File file) {
        if (file.exists())
            return load(file);
        return null;
    }

    public Config load(File file) {
        try {
            return loadUrl(file.toURI().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private Config loadUrl(URL url) {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            return mapper.readValue(url, Config.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ClassLoader classLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
