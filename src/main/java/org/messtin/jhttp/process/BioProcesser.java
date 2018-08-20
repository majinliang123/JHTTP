package org.messtin.jhttp.process;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.messtin.jhttp.config.Constants;
import org.messtin.jhttp.entity.HttpRequest;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;

/**
 * The processor for block server
 * <p>
 * The request message structure:
 * http://www.runoob.com/http/http-messages.html
 */
public class BioProcesser extends Processor {
    private static final Logger logger = LogManager.getLogger(BioProcesser.class);

    public BioProcesser(Socket socket) {
        super(socket);
    }

    @Override
    protected void buildRequest() {

        try {
            InputStreamReader reader =
                    new InputStreamReader(new BufferedInputStream(socket.getInputStream()));
            StringBuilder reqStrBuilder = new StringBuilder();

            char[] c = new char[4096];
            int len = -1;
            do {
                len = reader.read(c);
                reqStrBuilder.append(c, 0, len);
            } while (len == 4096);

            /**
             * There are two parts in the request
             * one is header and the other is body
             * They are split by \r\n\r\n
             * But sometime there are no body for get request
             */
            String reqStr = reqStrBuilder.toString();
            String[] reqArr = reqStr.split("\r\n\r\n");
            buildHeaders(reqArr[0]);
            if (reqArr.length > 1) {
                buildBody(reqArr[1]);
            }

        } catch (IOException ex) {
            logger.error("Failed read input stream from address: {}",
                    socket.getRemoteSocketAddress());
        }
    }

    private void buildHeaders(String headStr) {
        String[] headers = headStr.split("\r\n");

        String statusStr = headers[0];
        String[] status = statusStr.split("\\s+");
        request.setMethod(HttpRequest.Method.valueOf(status[0]));
        request.setUrl(status[1]);
        request.setVersion(status[2]);

        Arrays.stream(headers)
                .skip(1)
                .forEach(headerStr -> {
                    int colonIndex = headerStr.indexOf(Constants.COLON);
                    String key = headerStr.substring(0, colonIndex).trim();
                    String val = headerStr.substring(colonIndex + 1).trim();
                    request.getHeaders().put(key, val);
                });
    }

    private void buildBody(String bodyStr) {
        request.setBody(bodyStr);
    }
}
