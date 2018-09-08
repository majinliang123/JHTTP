package org.messtin.jhttp.container;

import org.messtin.jhttp.servlet.AbstractHttpServlet;

import java.util.HashMap;
import java.util.Map;

/**
 * The container of class about {@link AbstractHttpServlet}
 *
 * @author majinliang
 */
public final class ServletContainer {
    /**
     * A map from:
     * path {@link String} -> class of servlet
     */
    private static Map<String, Class<AbstractHttpServlet>> servletMap;

    /**
     * Lazy init when have httpServlet.
     * New httpServlet will replace old httpServlet if have same path.
     *
     * @param path        the path of servlet
     * @param httpServlet servlet
     */
    public static void put(String path, Class<AbstractHttpServlet> httpServlet) {
        if (servletMap == null) {
            servletMap = new HashMap<>();
        }
        servletMap.put(path, httpServlet);
    }

    public static Class<AbstractHttpServlet> get(String path) {
        if (servletMap == null) {
            return null;
        }
        return servletMap.get(path);
    }
}
