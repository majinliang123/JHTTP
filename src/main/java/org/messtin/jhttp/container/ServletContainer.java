package org.messtin.jhttp.container;

import org.messtin.jhttp.entity.JServlet;

import java.util.HashMap;
import java.util.Map;

public final class ServletContainer {
    private static Map<String, JServlet> servletMap;

    // lazy init when have servlet.
    // new servlet will replace old servlet if have same path.
    public static void put(String path, JServlet jServlet) {
        if (servletMap == null) {
            servletMap = new HashMap<>();
        }
        servletMap.put(path, jServlet);
    }

    public static JServlet get(String path) {
        if (servletMap == null) {
            return null;
        }
        return servletMap.get(path);
    }
}
