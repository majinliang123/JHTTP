package org.messtin.jhttp.entity;

import java.util.Date;

public class HttpResponse {
    private String version;
    private int statusCode;
    private String message;
    private Date date;
    private int contentLength;
    private String body = "";

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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String formatToString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(version);
        stringBuilder.append(" ");
        stringBuilder.append(statusCode);
        stringBuilder.append(message);
        stringBuilder.append("\r\n");
        stringBuilder.append("Date: ");
        stringBuilder.append(date);
        stringBuilder.append("\r\n");
        stringBuilder.append("Content-Length: ");
        stringBuilder.append(contentLength);
        stringBuilder.append("\r\n\r\n");
        stringBuilder.append(body);
        return stringBuilder.toString();
    }
}
