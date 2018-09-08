package org.messtin.jhttp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.messtin.jhttp.config.Config;
import org.messtin.jhttp.exception.ServerInitException;
import org.messtin.jhttp.servlet.AbstractHttpFilter;
import org.messtin.jhttp.servlet.AbstractHttpServlet;

/**
 * @author majinliang
 */
public final class Core {

    private static final Logger logger = LogManager.getLogger(Core.class);
    private static boolean INITIALIZED = false;

    /**
     * Initialize the server. When the server first start, it will set INITIALIZED as true and
     * when start again, it will throw {@link ServerInitException}
     *
     * @param clazzs the classes which extends {@link AbstractHttpFilter}
     *               and {@link AbstractHttpServlet}
     * @throws Exception the exception when init the server
     */
    public static void init(Class<?>... clazzs) throws Exception {

        if (!INITIALIZED) {
            logger.info("Initializing JHttp.");
            Init.init(clazzs);
            setInitialized();
            logger.info("JHttp Started.");
        } else {
            throw new ServerInitException("JHTTP has Initialized on port: " +
                    Config.PORT);
        }
    }

    private static void setInitialized() {
        INITIALIZED = true;
    }
}
