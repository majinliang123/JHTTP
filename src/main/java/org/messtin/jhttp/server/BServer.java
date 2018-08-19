package org.messtin.jhttp.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.messtin.jhttp.config.Config;
import org.messtin.jhttp.process.BProcesser;
import org.messtin.jhttp.process.Pool;
import org.messtin.jhttp.process.Processor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BServer implements Server {

    private static final Logger logger = LogManager.getLogger(BServer.class);

    private int port;
    private int timeout;
    private ServerSocket server;

    public BServer(int port, int timeout) throws IOException {
        this.port = port;
        this.timeout = timeout;
        server = new ServerSocket(Config.PORT);
        logger.info("Server listened on port: " + Config.PORT);
    }

    @Override
    public void service() throws IOException {
        Pool.submit(() -> {
            while (true) {
                try {
                    Socket socket = server.accept();
                    Processor processor = new BProcesser(socket);
                    Pool.submit(processor);
                } catch (IOException ex) {

                }

            }
        });


    }

}
