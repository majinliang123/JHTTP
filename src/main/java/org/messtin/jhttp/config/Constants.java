package org.messtin.jhttp.config;

import java.io.File;

public interface Constants {
    String HTTP_V1_1_VERSION = "HTTP/1.1";
    int STATUS_CODE_200 = 200;
    int STATUS_CODE_400 = 400;
    int STATUS_CODE_500 = 500;
    String STATUS_MESSAGE_OK ="OK";
    String STATUS_MESSAGE_BAD_REQUEST = "Bad request";
    String STATUS_MESSAGE_INTERNAL_SERVER_ERROR = "Internal Server Error";
    String CONTENT_TYPE = "Content-Type";
    String CONTENT_LENGTH = "Content-Length";
    String COOKIE = "Cookie";
    String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";

    String COLON = ":";
    String AMPERSAND = "&";
    String EQUAL = "=";
    String SEMICOLON = ";";
    String HTTP_SEPARATOR = "\r\n\r\n";
    String LINE_SEPARATOR = "\r\n";
}
