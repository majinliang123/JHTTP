package org.messtin.jhttp.exception;

public class RequestException extends ServerException {
    public RequestException(String message) {
        super(message);
    }

    public RequestException(Throwable ex) {
        super(ex);
    }
}
