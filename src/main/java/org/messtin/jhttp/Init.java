package org.messtin.jhttp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.messtin.jhttp.annotation.Filter;
import org.messtin.jhttp.annotation.Servlet;
import org.messtin.jhttp.config.Config;
import org.messtin.jhttp.container.FilterContainer;
import org.messtin.jhttp.container.ServletContainer;
import org.messtin.jhttp.servlet.AbstractHttpFilter;
import org.messtin.jhttp.servlet.AbstractHttpServlet;
import org.messtin.jhttp.server.BioServer;
import org.messtin.jhttp.server.Server;

import java.io.IOException;

/**
 * Use {@link #init(Class[])} to initialize the whole server
 *
 * @author majinliang
 */
public final class Init {

    private static final Logger logger = LogManager.getLogger(Init.class);

    public static void init(Class<?>... clazzs) throws IOException, ReflectiveOperationException {

        logger.info("Start register AbstractHttpServlet and AbstractHttpFilter.");
        for (int i = 0; i < clazzs.length; i++) {
            Class<?> clazz = clazzs[i];
            initServlet(clazz);
            initFilter(clazz);
        }

        logger.info("Start init Server.");
        initServer();
    }

    /**
     * Add the class which extends {@link AbstractHttpServlet} into {@link ServletContainer}
     * <p>
     * Will filter the class which extends {@link AbstractHttpServlet} and put its {@link Class}
     * into {@link ServletContainer}. Use the annotation path as its key.
     *
     * @param clazz the class extends {@link AbstractHttpServlet} and {@link AbstractHttpFilter}
     */
    private static void initServlet(Class<?> clazz) {
        if (AbstractHttpServlet.class.isAssignableFrom(clazz)) {
            Servlet servletAnnotation = clazz.getAnnotation(org.messtin.jhttp.annotation.Servlet.class);
            if (servletAnnotation != null) {
                Class<AbstractHttpServlet> httpServlet = (Class<AbstractHttpServlet>) clazz;
                ServletContainer.put(servletAnnotation.value(), httpServlet);
                logger.info("Registered AbstractHttpServlet {} >> {}.",
                        httpServlet.getName(), servletAnnotation.value());
            }
        }
    }

    /**
     * Add the object of he class which extends {@link AbstractHttpFilter} into {@link FilterContainer}
     * <p>
     * Will filter the class which extends {@link AbstractHttpFilter} and put its object of the class
     * into {@link FilterContainer}. Use the annotation path as its key.
     *
     * @param clazz the class extends {@link AbstractHttpServlet} and {@link AbstractHttpFilter}
     * @throws ReflectiveOperationException the exception when failed create instance use reflect
     */
    private static void initFilter(Class<?> clazz)
            throws ReflectiveOperationException {
        if (AbstractHttpFilter.class.isAssignableFrom(clazz)) {
            Filter filterAnnotation = clazz.getAnnotation(org.messtin.jhttp.annotation.Filter.class);
            if (filterAnnotation != null) {
                AbstractHttpFilter httpFilter = (AbstractHttpFilter) clazz.newInstance();
                FilterContainer.put(filterAnnotation.value(), httpFilter);
                logger.info("Registered AbstractHttpFilter {} >> {}.",
                        httpFilter.getClass().getName(), filterAnnotation.value());
            }
        }

    }

    /**
     * Initialize the server, it could use bio or nio
     *
     * @throws IOException the exception when initialize the server
     */
    private static void initServer() throws IOException {
        Server server = new BioServer(Config.PORT);
//        Server server = new NioServer(Config.PORT);
        server.service();
    }


}
