package com.messtin.jhttp;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JHTTP {

    private static final Logger logger = Logger.getLogger(JHTTP.class.getCanonicalName());
    private static final int THREAD_NUM = 50;
    private static final String INDEX_FILE = "index.html";

    private File rootDir;
    private int port;

    public JHTTP(File rootDir, int port) throws IOException {
        if (!rootDir.isDirectory()) {
            throw new IOException(rootDir + " does not exist as a directory.");
        }
        this.rootDir = rootDir;
        this.port = port;
    }

    public void start() throws IOException {
        ExecutorService pool = Executors.newFixedThreadPool(THREAD_NUM);
        try (ServerSocket server = new ServerSocket(port)) {
            logger.info("Accepting connections on port: " + server.getLocalPort());
            logger.info("Document Root: " + rootDir);

            while (true){
                try {
                    Socket request = server.accept();
                    Runnable r = new RequestProcessor(rootDir,INDEX_FILE, request);
                    pool.submit(r);
                } catch (IOException ex){
                    logger.log(Level.WARNING, "Error accepting connection", ex);
                }
            }

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
