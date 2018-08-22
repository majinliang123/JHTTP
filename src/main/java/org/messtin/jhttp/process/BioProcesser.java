package org.messtin.jhttp.process;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.messtin.jhttp.exception.HttpException;

import java.io.*;
import java.net.Socket;

/**
 * The processor for block server
 * <p>
 * The request message structure:
 * http://www.runoob.com/http/http-messages.html
 */
public class BioProcesser extends Processor {
    private static final Logger logger = LogManager.getLogger(BioProcesser.class);

    private Socket socket;

    public BioProcesser(Socket socket) {
        super(socket.getRemoteSocketAddress().toString());
        this.socket = socket;
    }

    @Override
    protected String buildRequestStr() {
        try {
            InputStreamReader reader =
                    new InputStreamReader(new BufferedInputStream(socket.getInputStream()));
            StringBuilder reqStrBuilder = new StringBuilder();

            char[] c = new char[4096];
            int len = -1;
            do {
                len = reader.read(c);
                reqStrBuilder.append(c, 0, len);
            } while (len == 4096);

            return reqStrBuilder.toString();
        } catch (IOException ex) {
            logger.error("Failed read input stream from address: {}",
                    socket.getRemoteSocketAddress());
            throw new HttpException("Failed read request.");
        }
    }

    @Override
    protected void sendReponse() {
        try (OutputStreamWriter writer =
                     new OutputStreamWriter(new BufferedOutputStream(socket.getOutputStream()))) {

            writer.write(response.formatToString().toCharArray());
            writer.flush();
        } catch (IOException ex) {
            logger.error("Failed to write response to address: {}", socket.getRemoteSocketAddress());
        }
    }

    @Override
    protected void close() {
        try {
            socket.close();
        } catch (Exception ex) {
            logger.error("Failed to close socket with address: {}", socket.getRemoteSocketAddress());
        }
    }
}
