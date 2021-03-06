package org.messtin.jhttp.process;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.messtin.jhttp.config.Config;
import org.messtin.jhttp.config.Constants;
import org.messtin.jhttp.entity.HttpRequest;
import org.messtin.jhttp.exception.RequestException;
import org.messtin.jhttp.exception.ResponseException;
import org.messtin.jhttp.util.HttpUtil;

import java.io.*;
import java.net.Socket;

/**
 * The processor for block server
 *
 * @author majinliang
 */
public class BioProcessor extends AbstractProcessor {
    private static final Logger logger = LogManager.getLogger(BioProcessor.class);

    private Socket socket;

    public BioProcessor(Socket socket) {
        super(socket.getRemoteSocketAddress().toString());
        this.socket = socket;
    }

    @Override
    protected byte[] buildHeaders() throws IOException, RequestException {
        InputStream in = new BufferedInputStream(socket.getInputStream());

        boolean isReadEnd = false;
        byte[] b = new byte[Config.MAX_HEADER];
        int len = in.read(b);
        if (len < Config.MAX_HEADER) {
            isReadEnd = true;
        }

        String reqStr = new String(b);

        if (!reqStr.contains(Constants.HTTP_SEPARATOR)) {
            throw new RequestException("The request header is illegal.");
        }
        String[] contextArr = reqStr.split(Constants.HTTP_SEPARATOR);
        String headerStr = contextArr[0];
        String bodyStr = null;
        if (contextArr.length > 1) {
            bodyStr = contextArr[1];
        }

        HttpUtil.buildHeader(headerStr, request);
        if (request.getMethod().equals(HttpRequest.Method.POST)) {
            byte[] currentBody = bodyStr.getBytes();
            if (isReadEnd) {
                return currentBody;
            } else {
                int bodyLen = Integer.parseInt(request.getHeaders().get(Constants.CONTENT_LENGTH));
                int leftLen = bodyLen - currentBody.length;
                byte[] leftBody = new byte[leftLen];
                in.read(leftBody);
                return HttpUtil.mergeByte(currentBody, leftBody);
            }
        } else {
            return new byte[0];
        }
    }

    @Override
    protected void sendResponse() throws ResponseException {
        try (OutputStreamWriter writer =
                     new OutputStreamWriter(new BufferedOutputStream(socket.getOutputStream()))) {

            writer.write(response.formatToString().toCharArray());
            writer.flush();
        } catch (IOException ex) {
            logger.error("Failed to write response to address: {}", socket.getRemoteSocketAddress());
            throw new ResponseException(ex);
        }
    }

    @Override
    protected void close() throws ResponseException {
        try {
            socket.close();
        } catch (Exception ex) {
            logger.error("Failed to close socket with address: {}", socket.getRemoteSocketAddress());
            throw new ResponseException(ex);
        }
    }
}
