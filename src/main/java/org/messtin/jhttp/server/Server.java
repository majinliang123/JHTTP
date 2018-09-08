package org.messtin.jhttp.server;

/**
 * Server will be used when initialize
 *
 * @author majinliang
 */
public interface Server {
    /**
     * Accept request from user and
     * will process the request
     */
    void service();
}
