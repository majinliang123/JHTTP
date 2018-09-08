package org.messtin.jhttp.servlet;

import org.messtin.jhttp.entity.HttpRequest;
import org.messtin.jhttp.entity.HttpResponse;

/**
 * When create a Servlet, need extends {@link AbstractHttpServlet}
 * and implements its {@link #doService(HttpRequest, HttpResponse)} function
 * and add {@link org.messtin.jhttp.annotation.Servlet} annotation
 *
 * @author majinliang
 */
public abstract class AbstractHttpServlet {
    /**
     * service the request
     *
     * @param request  the request from user
     * @param response the response build by server
     */
    public abstract void doService(HttpRequest request, HttpResponse response);
}
