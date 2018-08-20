package org.messtin.jhttp.servlet;

import org.messtin.jhttp.entity.HttpRequest;
import org.messtin.jhttp.entity.HttpResponse;

public abstract class HttpServlet {
    public abstract void doService(HttpRequest request, HttpResponse response);
}
