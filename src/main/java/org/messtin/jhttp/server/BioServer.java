package org.messtin.jhttp.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.messtin.jhttp.config.Config;
import org.messtin.jhttp.process.BioProcesser;
import org.messtin.jhttp.process.Pool;
import org.messtin.jhttp.process.Processor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BioServer implements Server {

    private static final Logger logger = LogManager.getLogger(BioServer.class);

    private ServerSocket server;

    public BioServer(int port) throws IOException {
        server = new ServerSocket(port);
        logger.info("Server listened on port: " + port);
    }

    @Override
    public void service() throws IOException {
        Pool.submit(() -> {
            while (true) {
                try {
                    Socket socket = server.accept();
                    logger.info("Handling request from: {}",
                            socket.getRemoteSocketAddress());

                    Processor processor = new BioProcesser(socket);
                    Pool.submit(processor);
                } catch (IOException ex) {
                    logger.error("Failed accept request.");
                    logger.error(ex);
                }

            }
        });


    }

}
