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
package project;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ProjectExtractor {
    public static void extract(File folder) {
        folder.mkdirs();
        extractTestSourceFile(folder);
        extractPomFile(folder);
    }

    private static void extractPomFile(File folder) {
        ByteArrayOutputStream templateContent = new ByteArrayOutputStream();
        copy(Thread.currentThread().getContextClassLoader().getResourceAsStream("pom.xml.template"), templateContent);

        String pomFileContent = new String(templateContent.toByteArray());

        try {
            FileOutputStream output = new FileOutputStream(new File(folder, "pom.xml"));
            copy(new ByteArrayInputStream(pomFileContent.getBytes()), output);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void extractTestSourceFile(File folder) {
        File sourceDir = new File(folder, "src/test/java/example");
        sourceDir.mkdirs();
        extractFile(sourceDir, "ExampleJunit4Test.java");
        extractFile(sourceDir, "ExampleJunit3Test.java");
    }

    private static void extractFile(File sourceDir, String filename) {
        File testFile = new File(sourceDir, filename);

        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
        try {
            copy(input, new FileOutputStream(testFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void copy(InputStream input, OutputStream output) {
        byte[] buffer = new byte[1024];
        int len = -1;
        try {
            while ((len = input.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            close(input);
            close(output);
        }
    }

    private static void close(InputStream input) {
        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {

            }
        }
    }

    private static void close(OutputStream output) {
        if (output != null) {
            try {
                output.flush();
                output.close();
            } catch (IOException e) {

            }
        }
    }
}
