package org.messtin.jhttp.exception;

/**
 * The exception when request is not right
 *
 * @author majinliang
 */
public class RequestException extends ServerException {
    public RequestException(String message) {
        super(message);
    }

    public RequestException(Throwable ex) {
        super(ex);
    }
}
