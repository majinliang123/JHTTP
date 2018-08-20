package org.messtin.jhttp.servlet;

import org.messtin.jhttp.entity.HttpRequest;
import org.messtin.jhttp.entity.HttpResponse;

public abstract class HttpFilter {
    public abstract void doFilter(HttpRequest request, HttpResponse response);
}
