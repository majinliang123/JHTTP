package org.messtin.jhttp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.messtin.jhttp.config.Config;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Core {

    private static final Logger logger = LogManager.getLogger(Core.class);

    public void init(List<Class<?>> clazzs) throws Exception {

        logger.info("Initializing JHttp.");

        initServer();
        initServlet();
        initFilter();

        logger.info("JHttp Started.");
    }

    private void initServer() throws IOException {
        ServerSocket server = new ServerSocket(Config.PORT);
        server.setSoTimeout(Config.TIME_OUT);
    }

    private void initServlet() {

    }

    private void initFilter() {

    }
}
