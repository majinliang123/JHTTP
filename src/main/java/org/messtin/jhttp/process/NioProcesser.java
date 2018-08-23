package org.messtin.jhttp.process;

import org.messtin.jhttp.config.Config;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class NioProcesser extends Processor {

    private SelectionKey key;
    private String address;

    public NioProcesser(SelectionKey key, String address) throws IOException {
        super(address);
        this.key = key;
        this.address = address;
    }

    @Override
    protected void sendReponse() throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        socketChannel.write(ByteBuffer.wrap(response.formatToString().getBytes(Config.CHARSET)));
    }

    @Override
    protected void close() throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        socketChannel.close();
    }

    @Override
    protected String buildRequestStr() throws IOException{
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.clear();
        String receive = null;
        if ((socketChannel.read(buffer)) != -1) {
            buffer.flip();
            receive = Charset.forName(Config.CHARSET).newDecoder().decode(buffer).toString();
        }
        return receive;
    }
}
