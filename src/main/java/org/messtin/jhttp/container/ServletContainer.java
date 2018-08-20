package org.messtin.jhttp.container;

import org.messtin.jhttp.servlet.HttpServlet;

import java.util.HashMap;
import java.util.Map;

public final class ServletContainer {
    private static Map<String, HttpServlet> servletMap;

    // lazy init when have httpServlet.
    // new httpServlet will replace old httpServlet if have same path.
    public static void put(String path, HttpServlet httpServlet) {
        if (servletMap == null) {
            servletMap = new HashMap<>();
        }
        servletMap.put(path, httpServlet);
    }

    public static HttpServlet get(String path) {
        if (servletMap == null) {
            return null;
        }
        return servletMap.get(path);
    }
}
