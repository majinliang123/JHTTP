package org.messtin.jhttp.exception;

/**
 * The highest exception of the server
 *
 * @author majinliang
 */
public class ServerException extends Exception {
    public ServerException(String message) {
        super(message);
    }

    public ServerException(Throwable ex) {
        super(ex);
    }
}
