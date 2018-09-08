package org.messtin.jhttp.servlet;

import org.messtin.jhttp.entity.HttpRequest;
import org.messtin.jhttp.entity.HttpResponse;

/**
 * When create a Filter, need extends {@link AbstractHttpFilter}
 * and implements its {@link #doFilter(HttpRequest, HttpResponse)} function
 * and add {@link org.messtin.jhttp.annotation.Filter} annotation
 *
 * @author majinliang
 */
public abstract class AbstractHttpFilter {
    /**
     * filter a request
     *
     * @param request  the request from user
     * @param response the response build by server
     */
    public abstract void doFilter(HttpRequest request, HttpResponse response);
}
