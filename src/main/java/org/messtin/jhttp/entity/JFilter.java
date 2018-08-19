package org.messtin.jhttp.entity;

public abstract class JFilter {
    public abstract void doFilter(JHttpRequest request, JHttpResponse response);
}
