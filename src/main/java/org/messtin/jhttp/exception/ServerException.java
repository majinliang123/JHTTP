package org.messtin.jhttp.exception;

public class ServerException extends Exception {
    public ServerException(String message) {
        super(message);
    }

    public ServerException(Throwable ex){
        super(ex);
    }
}
