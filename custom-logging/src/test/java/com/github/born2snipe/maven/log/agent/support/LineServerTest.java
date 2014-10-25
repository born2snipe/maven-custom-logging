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
package com.github.born2snipe.maven.log.agent.support;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class LineServerTest {

    private static final int PORT = 1234;
    private LineServer server;
    private RecordingLineListener listener;
    private OutputStream output;

    @Before
    public void setUp() throws Exception {
        listener = new RecordingLineListener();

        server = new LineServer(PORT);
        server.addListener(listener);
        server.start();

        Socket socket = null;
        try {
            socket = new Socket("localhost", PORT);
            output = socket.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void shouldNotifyTheListenerWhenALineIsReceived() {
        send("1");
        send("2");
        send("3");

        pause();

        assertEquals(Arrays.asList("1", "2", "3"), listener.lines);
    }

    private void pause() {
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {

        }
    }

    private void send(String line) {
        try {
            String s = line + "\n";
            output.write(s.getBytes());
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class RecordingLineListener implements LineListener {
        private ArrayList<String> lines = new ArrayList<String>();

        @Override
        public void lineReceived(String line) {
            lines.add(line);
        }
    }
}