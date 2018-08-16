package com.messtin.jhttp;

import java.io.*;
import java.net.Socket;
import java.net.URLConnection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestProcessor implements Runnable {

    private static final Logger logger = Logger.getLogger(RequestProcessor.class.getCanonicalName());

    private File rootDir;
    private String indexFileName;
    private Socket connection;

    public RequestProcessor(File rootDir, String indexFileName, Socket connection) throws IOException {
        this.rootDir = rootDir.getCanonicalFile();
        this.indexFileName = indexFileName;
        this.connection = connection;
    }

    @Override
    public void run() {

        String root = rootDir.getPath();
        try {
            OutputStream raw = new BufferedOutputStream(connection.getOutputStream());
            Writer out = new OutputStreamWriter(raw);
            Reader in = new InputStreamReader(
                    new BufferedInputStream(
                            connection.getInputStream()),
                    "US-ASCII");

            StringBuilder requestLine = new StringBuilder();
            while (true){
                int c = in.read();
                if(c == '\r' || c == '\n') break;
                requestLine.append((char)c);
            }

            String get = requestLine.toString();
            logger.info(connection.getRemoteSocketAddress() + " " + get);

            String[] tokens = get.split("\\s+");
            String method = tokens[0];
            String version = "";
            if(method.equals("GET")){
                String fileName = tokens[1];
                if(fileName.endsWith("/")) fileName += indexFileName;
                String contentType = URLConnection.getFileNameMap().getContentTypeFor(fileName);
                version = tokens[2];
                File theFile = new File(rootDir, fileName);

                byte[] data = new byte[]{'a','b','c','d'};
                sendHeader(out, "HTTP/1.1 200 OK", "text/plain", data.length);
                raw.write(data);

                raw.close();

            }


        } catch (IOException ex) {
            logger.log(Level.WARNING, "Error talking to " + connection.getRemoteSocketAddress());
        } finally {
            try {
                connection.close();
            } catch (IOException ex) {
            }
        }
    }

    private void sendHeader(Writer out, String responseCode, String contentType, int length) throws IOException{
        out.write(responseCode + "\r\n");
        Date now= new Date();
        out.write("Date: " + now + "\r\n");
        out.write("Server: JHTTP 2.0 \r\n");
        out.write("Content-length: " + length + "\r\n");
        out.write("Content-type: " + contentType + "\r\n");
        out.flush();
    }
}
