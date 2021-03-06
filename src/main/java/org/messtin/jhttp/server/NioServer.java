package org.messtin.jhttp.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.messtin.jhttp.pool.ThreadPool;
import org.messtin.jhttp.process.NioProcessor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * The server for nio
 * @author majinliang
 */
public class NioServer implements Server {
    private static final Logger logger = LogManager.getLogger(NioServer.class);

    private Selector selector;

    public NioServer(int port) throws IOException {
        selector = Selector.open();
        ServerSocketChannel server = ServerSocketChannel.open();
        server.configureBlocking(false);
        ServerSocket serverSocket = server.socket();
        serverSocket.bind(new InetSocketAddress(port));
        server.register(selector, SelectionKey.OP_ACCEPT);

        logger.info("Server listened on port: " + port);
    }

    @Override
    public void service() {
        ThreadPool.submit(() -> {
            while (true) {
                try {
                    selector.select();
                    Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                    while (iter.hasNext()) {
                        SelectionKey key = iter.next();
                        if (key.isAcceptable()) {
                            SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(key.selector(), SelectionKey.OP_READ);
                        } else if (key.isReadable()) {
                            String remoteAddress = ((SocketChannel) key.channel()).getRemoteAddress().toString();
                            logger.info("Handling request from: {}", remoteAddress);
                            new NioProcessor(key, remoteAddress).process();
                            key.cancel();
                        }
                        iter.remove();
                    }
                } catch (IOException e) {
                    logger.error("Failed accept request.");
                    e.printStackTrace();
                }
            }
        });
    }
}
