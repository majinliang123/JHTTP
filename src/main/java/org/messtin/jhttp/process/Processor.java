package org.messtin.jhttp.process;

import org.messtin.jhttp.container.FilterContainer;
import org.messtin.jhttp.container.ServletContainer;
import org.messtin.jhttp.entity.JFilter;
import org.messtin.jhttp.entity.JHttpRequest;
import org.messtin.jhttp.entity.JHttpResponse;
import org.messtin.jhttp.entity.JServlet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

public abstract class Processor {
    protected Socket socket;
    protected JHttpRequest request;
    protected JHttpResponse response;

    public Processor(Socket socket) {
        this.socket = socket;
        this.request = new JHttpRequest();
        this.response = new JHttpResponse();
    }

    public void process() {
        buildRequest();
        doFilter(request, response);
        doServlet(request, response);
        buildReponse();
        sendReponse();
        closeSocket();
    }

    protected abstract void buildRequest();

    private void buildReponse() {
        response.setVersion("HTTP/1.1");
        response.setStatusCode(200);
        response.setMessage("OK");
        response.setDate(new Date());
        response.setContentLength(response.getBody().getBytes().length);
    }

    private void doFilter(JHttpRequest request, JHttpResponse response) {
        String url = request.getUrl();
        JFilter jFilter = FilterContainer.get(url);
        if (jFilter != null) {
            jFilter.doFilter(request, response);
        }

    }

    private void doServlet(JHttpRequest request, JHttpResponse response) {
        String url = request.getUrl();
        JServlet jServlet = ServletContainer.get(url);
        if (jServlet != null) {
            jServlet.doService(request, response);
        }
    }

    private void sendReponse() {
        try (OutputStreamWriter out =
                     new OutputStreamWriter(new BufferedOutputStream(socket.getOutputStream()))) {

            out.write(response.formatToString().toCharArray());
            out.flush();
        } catch (IOException ex) {

        }
    }

    public void closeSocket() {
        try {
            socket.close();
        } catch (Exception ex) {

        }

    }
}
