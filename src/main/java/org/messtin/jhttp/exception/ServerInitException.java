package org.messtin.jhttp.exception;

/**
 * The exception when failed start the server
 *
 * @author majinliang
 */
public class ServerInitException extends ServerException {
    public ServerInitException(String message){
        super(message);
    }

    public ServerInitException(Throwable ex){
        super(ex);
    }
}
