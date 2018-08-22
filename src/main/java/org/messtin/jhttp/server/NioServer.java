package org.messtin.jhttp.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

public class NioServer implements Server {

    private ServerSocketChannel server;

    public NioServer(int port) {
        try {
            server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(port));
            server.configureBlocking(false);
        } catch (IOException ex) {

        }

    }

    @Override
    public void service() throws IOException {

    }
}
