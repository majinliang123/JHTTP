package org.messtin.jhttp.exception;

public class ResponseException extends ServerException{
    public ResponseException(String message) {
        super(message);
    }

    public ResponseException(Throwable ex) {
        super(ex);
    }
}
