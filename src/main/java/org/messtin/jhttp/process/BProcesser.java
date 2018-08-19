package org.messtin.jhttp.process;

import org.messtin.jhttp.entity.JHttpRequest;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;

/**
 * The processor for block server
 * <p>
 * The request message structure:
 * http://www.runoob.com/http/http-messages.html
 */
public class BProcesser extends Processor {

    public BProcesser(Socket socket) {
        super(socket);
    }

    @Override
    protected void buildRequest() {

        try {
            InputStreamReader in =
                    new InputStreamReader(new BufferedInputStream(socket.getInputStream()));
            StringBuilder reqStrBuilder = new StringBuilder();

            char[] c = new char[4096];
            int len = -1;
            do {
                len = in.read(c);
                reqStrBuilder.append(c, 0, len);

            } while (len == 4096);

            String reqStr = reqStrBuilder.toString();
            String[] reqArr = reqStr.split("\r\n\r\n");
            buildHeaders(reqArr[0]);
            if (reqArr.length > 1) {
                buildBody(reqArr[1]);
            }

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    private void buildHeaders(String headStr) {
        String[] headers = headStr.split("\r\n");

        String statusStr = headers[0];
        String[] status = statusStr.split("\\s+");
        request.setMethod(JHttpRequest.Method.valueOf(status[0]));
        request.setUrl(status[1]);
        request.setVersion(status[2]);

//        for (int i = 1; i < headers.length; i++) {
//            String headerStr = headers[i];
//            String[] keyVal = headerStr.split(":");
//            Map<String, String> reqHeaders = request.getHeaders();
//            reqHeaders.put(keyVal[0], keyVal[1]);
//        }

    }

    private void buildBody(String bodyStr) {
        request.setBody(bodyStr);
    }
}
