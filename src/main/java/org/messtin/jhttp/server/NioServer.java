package org.messtin.jhttp.server;

import org.messtin.jhttp.process.NioProcesser;
import org.messtin.jhttp.process.Pool;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioServer implements Server {

    private Selector selector;

    public NioServer(int port) {
        try {
            selector = Selector.open();
            ServerSocketChannel server = ServerSocketChannel.open();
            server.configureBlocking(false);
            ServerSocket serverSocket = server.socket();
            serverSocket.bind(new InetSocketAddress(port));
            server.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException ex) {
        }
    }

    @Override
    public void service() throws IOException {
        Pool.submit(() -> {
            while (true) {
                try {
                    selector.select();
                    Iterator<SelectionKey> iter = selector.selectedKeys().iterator();

                    while (iter.hasNext()) {
                        SelectionKey key = iter.next();
                        iter.remove();

                        if (key.isAcceptable()) {
                            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                            SocketChannel sc = ssc.accept();
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_READ);

                        } else if (key.isReadable()) {
                            SocketChannel sc = (SocketChannel) key.channel();
//                            sc.register(selector, SelectionKey.OP_WRITE);
                            Pool.submit(new NioProcesser(key));
                        }
//                        } else if(key.isWritable()){
//                            SocketChannel clientChannel = (SocketChannel) key.channel();
//                            clientChannel.close();
//                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
