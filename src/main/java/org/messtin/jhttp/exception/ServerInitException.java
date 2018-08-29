package org.messtin.jhttp.exception;

public class ServerInitException extends ServerException {
    public ServerInitException(String message){
        super(message);
    }

    public ServerInitException(Throwable ex){
        super(ex);
    }
}
