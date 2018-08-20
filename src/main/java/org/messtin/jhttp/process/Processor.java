package org.messtin.jhttp.process;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.messtin.jhttp.config.Config;
import org.messtin.jhttp.config.Constants;
import org.messtin.jhttp.container.FilterContainer;
import org.messtin.jhttp.container.ServletContainer;
import org.messtin.jhttp.servlet.HttpFilter;
import org.messtin.jhttp.entity.HttpRequest;
import org.messtin.jhttp.entity.HttpResponse;
import org.messtin.jhttp.servlet.HttpServlet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;
import java.util.List;

public abstract class Processor {
    private static final Logger logger = LogManager.getLogger(Processor.class);

    protected Socket socket;
    protected HttpRequest request;
    protected HttpResponse response;

    public Processor(Socket socket) {
        this.socket = socket;
        this.request = new HttpRequest();
        this.response = new HttpResponse();
    }

    public void process() {
        logger.info("Start process request from: {}", socket.getRemoteSocketAddress());
        buildRequest();
        doFilter(request, response);
        doServlet(request, response);
        buildReponse();
        sendReponse();
        closeSocket();
        logger.info("Complete process request from: {}", socket.getRemoteSocketAddress());
    }

    protected abstract void buildRequest();

    private void buildReponse() {
        response.setVersion(Constants.HTTP_V1_1_VERSION);
        response.setStatusCode(Constants.STATUS_CODE_200);
        response.setMessage(Constants.STATUS_MESSAGE_OK);
        response.setDate(new Date());
        response.setContentLength(response.getBody().getBytes().length);
    }

    private void doFilter(HttpRequest request, HttpResponse response) {
        String url = request.getUrl();
        List<HttpFilter> httpFilters= FilterContainer.get(url, Config.FILTER_ANT_MATCH);
        if (httpFilters != null) {
            httpFilters.stream()
                    .forEach(httpFilter ->{
                        httpFilter.doFilter(request, response);
                    });
        }
    }

    private void doServlet(HttpRequest request, HttpResponse response) {
        String url = request.getUrl();
        HttpServlet httpServlet = ServletContainer.get(url);
        if (httpServlet != null) {
            httpServlet.doService(request, response);
        }
    }

    private void sendReponse() {
        try (OutputStreamWriter writer =
                     new OutputStreamWriter(new BufferedOutputStream(socket.getOutputStream()))) {

            writer.write(response.formatToString().toCharArray());
            writer.flush();
        } catch (IOException ex) {
            logger.error("Failed to write response to address: {}", socket.getRemoteSocketAddress());
        }
    }

    public void closeSocket() {
        try {
            socket.close();
        } catch (Exception ex) {
            logger.error("Failed to close socket with address: {}", socket.getRemoteSocketAddress());
        }

    }
}
