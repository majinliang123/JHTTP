package org.messtin.jhttp.config;

/**
 * Config of the server
 *
 * @author majinliang
 */
public final class Config {
    public static int PORT = 8080;
    public static int TIME_OUT = 3000;
    public static int THREAD_NUM = 50;
    public static int SCHEDULE_NUM = 2;
    /**
     * use minutes as its time unit
     */
    public static int SESSION_VALIDATION_TIME = 360;
    /**
     * use minutes as its time unit
     */
    public static int SESSION_CHECK_DURING = 15;
    public static boolean FILTER_ANT_MATCH = true;
    public static int MAX_HEADER = 4096;
    public static String CHARSET = "UTF-8";
    public static boolean OPEN_SESSION = true;
    public static String SESSION_NAME = "jsession";
}
