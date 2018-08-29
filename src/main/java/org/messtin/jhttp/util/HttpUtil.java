package org.messtin.jhttp.util;

import org.messtin.jhttp.config.Config;
import org.messtin.jhttp.config.Constants;
import org.messtin.jhttp.entity.HttpRequest;

import java.util.Arrays;

public class HttpUtil {
    public static void buildHeader(String headerStr, HttpRequest request) {
        String[] headers = headerStr.split("\r\n");

        String statusStr = headers[0];
        String[] status = statusStr.split("\\s+");
        request.setMethod(HttpRequest.Method.valueOf(status[0]));
        request.setUrl(status[1]);
        request.setVersion(status[2]);

        Arrays.stream(headers)
                .skip(1)
                .forEach(header -> {
                    int colonIndex = header.indexOf(Constants.COLON);
                    String key = header.substring(0, colonIndex).trim();
                    String val = header.substring(colonIndex + 1).trim();
                    request.getHeaders().put(key, val);
                });

        String cookieStr = request.getHeaders().get(Constants.COOKIE);
        if(cookieStr == null)
            return;
        String[] cookieArr = cookieStr.split(Constants.SEMICOLON);
        for (String cookie : cookieArr) {
            if (cookie.contains(Config.SESSION_NAME)) {
                String sessionId = cookie.replace(Config.SESSION_NAME + Constants.EQUAL, "");
                request.setSessionId(sessionId);
            }
        }

    }

    public static byte[] mergeByte(byte[] src1, byte[] src2) {
        byte[] dest = new byte[src1.length + src2.length];
        System.arraycopy(src1, 0, dest, 0, src1.length);
        System.arraycopy(src2, 0, dest, src1.length, src2.length);
        return dest;
    }
}
