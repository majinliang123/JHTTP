package org.messtin.jhttp.exception;

/**
 * The exception when failed build response
 *
 * @author majinliang
 */
public class ResponseException extends ServerException{
    public ResponseException(String message) {
        super(message);
    }

    public ResponseException(Throwable ex) {
        super(ex);
    }
}
