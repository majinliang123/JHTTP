package org.messtin.jhttp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.messtin.jhttp.annotation.Filter;
import org.messtin.jhttp.annotation.Servlet;
import org.messtin.jhttp.config.Config;
import org.messtin.jhttp.container.FilterContainer;
import org.messtin.jhttp.container.ServletContainer;
import org.messtin.jhttp.server.NioServer;
import org.messtin.jhttp.servlet.HttpFilter;
import org.messtin.jhttp.servlet.HttpServlet;
import org.messtin.jhttp.server.BioServer;
import org.messtin.jhttp.server.Server;

import java.io.IOException;

public final class Init {

    private static final Logger logger = LogManager.getLogger(Init.class);

    public static void init(Class<?>... clazzs) throws IOException, ReflectiveOperationException {

        logger.info("Start register HttpServlet and HttpFilter.");
        for (int i = 0; i < clazzs.length; i++) {
            Class<?> clazz = clazzs[i];
            initServlet(clazz);
            initFilter(clazz);
        }

        logger.info("Start init Server.");
        initServer();
    }

    private static void initServlet(Class<?> clazz) {
        if (HttpServlet.class.isAssignableFrom(clazz)) {
            Servlet servletAnnotation = clazz.getAnnotation(org.messtin.jhttp.annotation.Servlet.class);
            if (servletAnnotation != null) {
                Class<HttpServlet> httpServlet = (Class<HttpServlet>)clazz;
                ServletContainer.put(servletAnnotation.value(), httpServlet);
                logger.info("Registered HttpServlet {} >> {}.",
                        httpServlet.getName(), servletAnnotation.value());
            }
        }
    }

    private static void initFilter(Class<?> clazz)
            throws ReflectiveOperationException {
        if (HttpFilter.class.isAssignableFrom(clazz)) {
            Filter filterAnnotation = clazz.getAnnotation(org.messtin.jhttp.annotation.Filter.class);
            if (filterAnnotation != null) {
                HttpFilter httpFilter = (HttpFilter) clazz.newInstance();
                FilterContainer.put(filterAnnotation.value(), httpFilter);
                logger.info("Registered HttpFilter {} >> {}.",
                        httpFilter.getClass().getName(), filterAnnotation.value());
            }
        }

    }

    private static void initServer() throws IOException {
//        Server server = new BioServer(Config.PORT);
        Server server = new NioServer(Config.PORT);
        server.service();
    }


}
