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
import java.net.InetSocketAddress;
import java.net.Socket;

import static com.github.born2snipe.maven.log.agent.support.LineServerManager.manage;
import static org.junit.Assert.assertTrue;

public class LineServerManagerTest {
    private LineListener lineProcessor = new NoOpLineListener();

    @Before
    public void setUp() throws Exception {
        LineServerManager.PORT = 63840;
    }

    @After
    public void tearDown() throws Exception {
        manage(buildSuccess(), lineProcessor);
    }

    @Test
    public void shouldShutdownTheServerWhenTheBuildFails() {
        manage(surefire(), lineProcessor);
        manage(buildFailure(), lineProcessor);
        assertTrue(canNotConnectToServer());
    }

    @Test
    public void shouldShutdownTheServerWhenTheBuildIsASuccess() {
        manage(surefire(), lineProcessor);
        manage(buildSuccess(), lineProcessor);
        assertTrue(canNotConnectToServer());
    }

    @Test
    public void shouldNotBlowUpTryingToStartTheServerAgainIfTheSurefireIsRanAgain() {
        manage(surefire(), lineProcessor);
        manage(surefire(), lineProcessor);
    }

    @Test
    public void shouldNotKillTheServerIfTheSurefireIsRanAgain() {
        manage(surefire(), lineProcessor);
        manage(surefire(), lineProcessor);
        assertTrue(canConnectToServer());
    }

    @Test
    public void shouldStartTheServerWhenTheSurefirePluginIsAboutToStart() {
        manage(surefire(), lineProcessor);

        assertTrue(canConnectToServer());
    }

    @Test
    public void shouldNotStartForJustAnyPluginStarting() {
        manage(plugin("some-other-plugin"), lineProcessor);

        assertTrue(canNotConnectToServer());
    }

    private String buildFailure() {
        return "BUILD FAILURE";
    }

    private String surefire() {
        return plugin("surefire");
    }

    private String plugin(String pluginName) {
        return "--- "+ pluginName +" ---";
    }

    private boolean canNotConnectToServer() {
        return !canConnectToServer();
    }

    private boolean canConnectToServer() {
        Socket socket = null;
        try {
            socket = new Socket();
            socket.setReuseAddress(true);
            socket.connect(new InetSocketAddress("localhost", LineServerManager.PORT));
            return socket.isConnected();
        } catch (IOException e) {
            return false;
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {

                }
            }
        }
    }

    private String buildSuccess() {
        return "BUILD SUCCESS";
    }

    private class NoOpLineListener implements LineListener {
        @Override
        public void lineReceived(String line) {

        }
    }
}