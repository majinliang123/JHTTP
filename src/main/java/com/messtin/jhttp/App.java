package com.messtin.jhttp;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    private static final Logger logger = Logger.getLogger(App.class.getCanonicalName());

    public static void main(String[] args){

        File docRoot;
        try {
            docRoot = new File(args[0]);
        }catch (ArrayIndexOutOfBoundsException ex){
            System.out.println("Usage: java JHTTP docroot port");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(args[1]);
            if(port < 0 || port > 65535){
                port = 80;
            }
        } catch (RuntimeException ex){
            port = 80;
        }

        try {
            JHTTP webServer = new JHTTP(docRoot, port);
            webServer.start();
        } catch (IOException ex){
            logger.log(Level.SEVERE, "Server could not start...");
        }
    }
}
