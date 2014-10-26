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
package com.github.born2snipe.maven.log.agent;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class SurefirePluginAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new ReplaceMavenLoggerWithAnsiLogger());
    }

    static class ReplaceMavenLoggerWithAnsiLogger implements ClassFileTransformer {
        public byte[] transform(ClassLoader classLoader, String s, Class<?> aClass, ProtectionDomain protectionDomain, byte[] bytes) throws IllegalClassFormatException {
            if (s.contains("surefire")) {
                System.out.println(s);
            }


            if (s.equals("org/apache/maven/surefire/report/DefaultDirectConsoleReporter")) {
                return readClassBytes("org/apache/maven/surefire/report/DefaultDirectConsoleReporter.class");
            }

            return null;
        }

        private byte[] readClassBytes(String classSuffix) {
            InputStream input = getClass().getClassLoader().getResourceAsStream(classSuffix);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            try {
                while ((len = input.read(buffer)) != -1) {
                    output.write(buffer, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return output.toByteArray();
        }
    }
}