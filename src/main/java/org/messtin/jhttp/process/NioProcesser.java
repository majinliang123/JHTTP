package org.messtin.jhttp.process;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class NioProcesser extends Processor {

    private SelectionKey key;
    private SocketChannel socketChannel;
    public NioProcesser(SelectionKey key) throws IOException {
        super(((SocketChannel)key.channel()).getRemoteAddress().toString());
        this.key = key;
        this.socketChannel = (SocketChannel) key.channel();
    }
    @Override
    protected void sendReponse() {
        try {
            socketChannel.write(ByteBuffer.wrap(response.formatToString().getBytes()));
        }catch (IOException io){

        }

    }

    @Override
    protected void close() {
        try {
            socketChannel.close();
        }catch (IOException io){

        }

    }

    @Override
    protected String buildRequestStr() {
        final int MAX_LENGTH = 4096 * 20;
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(512);
            int len = socketChannel.read(byteBuffer);
            if(len < MAX_LENGTH){
                byteBuffer.flip();
                byte[] data = new byte[byteBuffer.remaining()];
                byteBuffer.get(data);
                return new String(data);

            }else {
                return null;
            }

        } catch (IOException ex){

        }
        return null;
    }
}
