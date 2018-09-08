package org.messtin.jhttp.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author majinliang
 */
public class HttpRequest {

    public enum Method {
        /**
         * GET method in http
         */
        GET,

        /**
         * POST method in http
         */
        POST
    }

    public HttpRequest(){
        this.headers = new HashMap<>();
    }

    private String url;
    private Method method;
    private String version;
    private byte[] body;
    private String sessionId;
    private Map<String, String> query;
    private Map<String, List<Object>> params;
    private Map<String, String> headers;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getQuery() {
        return query;
    }

    public void setQuery(Map<String, String> query) {
        this.query = query;
    }

    public Map<String, List<Object>> getParams() {
        return params;
    }

    public void setParams(Map<String, List<Object>> params) {
        this.params = params;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
