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

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class LineServer {
    private static final int PORT = 36480;
    private NioSocketAcceptor socketAcceptor;
    private int port;

    public LineServer() {
        this(PORT);
    }

    public LineServer(int port) {
        this.port = port;
        socketAcceptor = new NioSocketAcceptor();
        socketAcceptor.setReuseAddress(true);
        socketAcceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.defaultCharset())));

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                LineServer.this.stop();
            }
        });

    }

    public void start() {
        try {
            socketAcceptor.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        socketAcceptor.dispose(true);
    }

    public void addListener(final LineListener listener) {
        socketAcceptor.setHandler(new IoHandlerAdapter() {
            @Override
            public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
                session.close(true);
            }

            @Override
            public void messageReceived(IoSession session, Object message) throws Exception {
                listener.lineReceived(message.toString());
            }
        });
    }
}
