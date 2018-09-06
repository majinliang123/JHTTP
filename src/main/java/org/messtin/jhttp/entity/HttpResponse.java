package org.messtin.jhttp.entity;

import org.messtin.jhttp.config.Config;
import org.messtin.jhttp.config.Constants;

import java.util.Date;

public class HttpResponse {
    private String version;
    private int statusCode;
    private String message;
    private Date date;
    private int contentLength;
    private byte[] body;
    private String contentType = "text/plain";
    private String sessionId;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setBody(String body){
        this.body = body.getBytes();
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String formatToString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(version);
        stringBuilder.append(" ");
        stringBuilder.append(statusCode);
        stringBuilder.append(" ");
        stringBuilder.append(message);
        stringBuilder.append("\r\n");
        stringBuilder.append("Date: ");
        stringBuilder.append(date);
        stringBuilder.append("\r\n");
        stringBuilder.append("Content-Length: ");
        stringBuilder.append(contentLength);
        stringBuilder.append("\r\n");
        stringBuilder.append("Content-Type: ");
        stringBuilder.append(contentType);
        stringBuilder.append("\r\n");
        if(Config.OPEN_SESSION && sessionId != null){
            stringBuilder.append("Set-Cookie: ");
            stringBuilder.append(Config.SESSION_NAME + Constants.EQUAL +  sessionId);
        }
        stringBuilder.append("\r\n\r\n");
        stringBuilder.append(new String(body));
        return stringBuilder.toString();
    }
}
