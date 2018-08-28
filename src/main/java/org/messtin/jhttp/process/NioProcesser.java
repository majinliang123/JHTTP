package org.messtin.jhttp.process;

import org.messtin.jhttp.config.Config;
import org.messtin.jhttp.config.Constants;
import org.messtin.jhttp.entity.HttpRequest;
import org.messtin.jhttp.exception.HttpException;
import org.messtin.jhttp.util.HttpUtil;

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
    protected byte[] buildHeaders() throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(Config.MAX_HEADER);

        boolean isReadEnd = false;
        int len = socketChannel.read(buffer);
        if (len < Config.MAX_HEADER) {
            isReadEnd = true;
        }

        buffer.flip();
        byte[] b = new byte[len];
        buffer.get(b);
        String reqStr = new String(b);

        if (!reqStr.contains(Constants.HTTP_SEPARATOR)) {
            throw new HttpException("");
        }

        String[] contextArr = reqStr.split(Constants.HTTP_SEPARATOR);
        String headerStr = contextArr[0];
        String bodyStr = null;
        if(contextArr.length > 1){
            bodyStr = contextArr[1];
        }

        HttpUtil.buildHeader(headerStr, request);
        if (request.getMethod().equals(HttpRequest.Method.POST)) {
            byte[] currentBody = bodyStr.getBytes();
            if (isReadEnd) {
                return currentBody;
            }else {
                int bodyLen =Integer.parseInt(request.getHeaders().get(Constants.CONTENT_LENGTH));
                int leftLen = bodyLen - currentBody.length;
                ByteBuffer leftBuffer = ByteBuffer.allocate(leftLen);
                socketChannel.read(leftBuffer);
                byte[] leftBody = new byte[leftLen];
                return HttpUtil.mergeByte(currentBody, leftBody);
            }
        } else {
            return new byte[0];
        }

    }
}
