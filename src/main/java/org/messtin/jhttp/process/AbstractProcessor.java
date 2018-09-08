package org.messtin.jhttp.process;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.messtin.jhttp.config.Config;
import org.messtin.jhttp.config.Constants;
import org.messtin.jhttp.container.FilterContainer;
import org.messtin.jhttp.container.ServletContainer;
import org.messtin.jhttp.container.SessionContainer;
import org.messtin.jhttp.exception.RequestException;
import org.messtin.jhttp.exception.ResponseException;
import org.messtin.jhttp.servlet.AbstractHttpFilter;
import org.messtin.jhttp.entity.HttpRequest;
import org.messtin.jhttp.entity.HttpResponse;
import org.messtin.jhttp.servlet.AbstractHttpServlet;

import java.io.IOException;
import java.util.*;

/**
 * The core class to process a request.
 * It uses template pattern.
 * <p>
 * Every processor need extends this class and implement the abstract
 * because different io will read request as different way.
 *
 * @author majinliang
 */
public abstract class AbstractProcessor {
    private static final Logger logger = LogManager.getLogger(AbstractProcessor.class);

    private String remoteAddress;
    protected HttpRequest request;
    protected HttpResponse response;

    public AbstractProcessor(String remoteAddress) {
        this.remoteAddress = remoteAddress;
        this.request = new HttpRequest();
        this.response = new HttpResponse();
    }

    /**
     * Template pattern
     * <p>
     * When exception because of request, will send response with 400
     * When exception because of server, will send response with 500
     */
    public void process() {
        logger.info("Start process request from: {}", remoteAddress);
        try {
            buildRequest();
            doFilter(request, response);
            doServlet(request, response);
            buildResponse();
            sendResponse();
            close();
            logger.info("Complete process request from: {}", remoteAddress);
        } catch (RequestException ex) {
            buildErrorResponse(Constants.STATUS_CODE_400, ex.getMessage());
            try {
                sendResponse();
            } catch (ResponseException e) {
                e.printStackTrace();
            }
            logger.error(ex);
            ex.printStackTrace();
        } catch (ReflectiveOperationException ex) {
            buildErrorResponse(Constants.STATUS_CODE_500, ex.getMessage());
            try {
                sendResponse();
            } catch (ResponseException e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
        } catch (ResponseException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            logger.error("Failed to process request from: {}", remoteAddress);
            ex.printStackTrace();
        }

    }

    /**
     * The request message structure:
     * <a href="http://www.runoob.com/http/http-messages.html">Http Structure</a>
     *
     * @throws IOException      exception when read stream from client
     * @throws RequestException exception when the request is not unabridged
     */
    protected void buildRequest() throws IOException, RequestException {
        byte[] body = buildHeaders();
        if (body.length > 0) {
            request.setBody(body);
            buildBody(body);
        }
    }

    /**
     * Build request's head, and use the data length in header to build request body
     *
     * @return byte[] request body
     * @throws IOException      exception when read request because of IO
     * @throws RequestException exception when the request is not unabridged
     */
    abstract protected byte[] buildHeaders() throws IOException, RequestException;

    private void doFilter(HttpRequest request, HttpResponse response) {
        String url = request.getUrl();
        List<AbstractHttpFilter> httpFilters = FilterContainer.get(url, Config.FILTER_ANT_MATCH);
        if (httpFilters != null) {
            httpFilters.stream()
                    .forEach(httpFilter -> {
                        httpFilter.doFilter(request, response);
                    });
        }
    }

    private void doServlet(HttpRequest request, HttpResponse response) throws ReflectiveOperationException {
        String url = request.getUrl();
        Class<AbstractHttpServlet> httpServlet = ServletContainer.get(url);
        if (httpServlet != null) {
            httpServlet.newInstance().doService(request, response);
        }
    }

    private void buildResponse() {
        response.setVersion(Constants.HTTP_V1_1_VERSION);
        response.setStatusCode(Constants.STATUS_CODE_200);
        response.setMessage(Constants.STATUS_MESSAGE_OK);
        response.setDate(new Date());
        response.setContentLength(response.getBody().length);

        buildCookie();
    }

    /**
     * Send a response to client
     *
     * @throws ResponseException exception when failed to build response because of server error
     */
    protected abstract void sendResponse() throws ResponseException;

    /**
     * Close the connection or io with client
     *
     * @throws ResponseException
     */
    protected abstract void close() throws ResponseException;

    /**
     * other functions
     */
    private void buildBody(byte[] body) {
        if (Constants.APPLICATION_FORM_URLENCODED.equals(request.getHeaders().get(Constants.CONTENT_TYPE))) {
            String bodyStr = new String(body);
            Map<String, List<Object>> params = new HashMap<>();
            String[] paramArr = bodyStr.split(Constants.AMPERSAND);
            for (String paramStr : paramArr) {
                int index = paramStr.indexOf(Constants.EQUAL);
                List<Object> vals = new ArrayList<>();
                vals.add(paramStr.substring(index + 1));
                params.put(paramStr.substring(0, index), vals);
            }
        }
    }

    private void buildCookie() {
        String sessionId = request.getSessionId();
        if (sessionId != null && SessionContainer.get(sessionId) != null) {
            response.setSessionId(request.getSessionId());
        } else {
            sessionId = SessionContainer.create();
            response.setSessionId(sessionId);
        }
    }

    private void buildErrorResponse(int statusCode, String message) {
        response = new HttpResponse();
        response.setVersion(Constants.HTTP_V1_1_VERSION);
        if (statusCode >= 400 && statusCode < 500) {
            response.setStatusCode(Constants.STATUS_CODE_400);
            response.setMessage(Constants.STATUS_MESSAGE_BAD_REQUEST);
        } else if (statusCode >= 500) {
            response.setStatusCode(Constants.STATUS_CODE_500);
            response.setMessage(Constants.STATUS_MESSAGE_INTERNAL_SERVER_ERROR);
        }
        response.setBody(message.getBytes());
        response.setDate(new Date());
        response.setContentLength(response.getBody().length);

    }

}
