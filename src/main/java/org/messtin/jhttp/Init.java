package org.messtin.jhttp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.messtin.jhttp.annotation.Filter;
import org.messtin.jhttp.annotation.Servlet;
import org.messtin.jhttp.config.Config;
import org.messtin.jhttp.container.FilterContainer;
import org.messtin.jhttp.container.ServletContainer;
import org.messtin.jhttp.entity.JFilter;
import org.messtin.jhttp.entity.JServlet;
import org.messtin.jhttp.server.BServer;
import org.messtin.jhttp.server.Server;

import java.io.IOException;

public final class Init {

    private static final Logger logger = LogManager.getLogger(Init.class);

    public static void init(Class<?>... clazzs) throws IOException, ReflectiveOperationException {

        logger.info("Start register Servlet and Filter.");
        for (int i = 0; i < clazzs.length; i++) {
            Class<?> clazz = clazzs[i];
            initServlet(clazz);
            initFilter(clazz);
        }

        logger.info("Start init Server.");
        initServer();
    }

    private static void initServlet(Class<?> clazz)
            throws ReflectiveOperationException {
        if (JServlet.class.isAssignableFrom(clazz)) {
            Servlet servletAnnotation = clazz.getAnnotation(Servlet.class);
            if (servletAnnotation != null) {
                JServlet jServlet = (JServlet) clazz.newInstance();
                ServletContainer.put(servletAnnotation.value(), jServlet);
                logger.info("Registered servlet {} >> {}.",
                        jServlet.getClass().getName(), servletAnnotation.value());
            }
        }
    }

    private static void initFilter(Class<?> clazz)
            throws ReflectiveOperationException {
        if (JFilter.class.isAssignableFrom(clazz)) {
            Filter filterAnnotation = clazz.getAnnotation(Filter.class);
            if (filterAnnotation != null) {
                JFilter jFilter = (JFilter) clazz.newInstance();
                FilterContainer.put(filterAnnotation.value(), jFilter);
                logger.info("Registered filter {} >> {}.",
                        jFilter.getClass().getName(), filterAnnotation.value());
            }
        }

    }

    private static void initServer() throws IOException {
        Server server = new BServer(Config.PORT, Config.TIME_OUT);
        server.service();
    }


}
