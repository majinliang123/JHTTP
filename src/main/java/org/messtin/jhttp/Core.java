package org.messtin.jhttp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.messtin.jhttp.config.Config;
import org.messtin.jhttp.exception.HttpInitException;

public final class Core {

    private static final Logger logger = LogManager.getLogger(Core.class);
    private static boolean INITIALIZED = false;

    public static void init(Class<?>... clazzs) throws Exception {

        if (!INITIALIZED) {
            logger.info("Initializing JHttp.");
            Init.init(clazzs);
            setInitialized();
            logger.info("JHttp Started.");
        } else {
            throw new HttpInitException("JHTTP has Initialized on port: " +
                    Config.PORT);
        }
    }

    private static void setInitialized() {
        INITIALIZED = true;
    }
}
