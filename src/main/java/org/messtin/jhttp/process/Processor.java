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

import java.io.IOException;
import java.util.*;

public abstract class Processor {
    private static final Logger logger = LogManager.getLogger(Processor.class);

    protected  String remoteAddress;
    protected HttpRequest request;
    protected HttpResponse response;

    public Processor(String remoteAddress) {
        this.remoteAddress = remoteAddress;
        this.request = new HttpRequest();
        this.response = new HttpResponse();
    }

    public void process (){
        logger.info("Start process request from: {}", remoteAddress);
        try {
            buildRequest();
            doFilter(request, response);
            doServlet(request, response);
            buildReponse();
            sendReponse();
            close();
            logger.info("Complete process request from: {}", remoteAddress);
        }catch (Exception e){
            logger.error("Failed to process request from: {}", remoteAddress);
            logger.error(e);
        }

    }

    protected void buildRequest() throws IOException{
        /**
         * There are two parts in the request
         * one is header and the other is body
         * They are split by \r\n\r\n
         * But sometime there are no body for get request
         */
        String reqStr = buildRequestStr();
        String[] reqArr = reqStr.split("\r\n\r\n");
        buildHeaders(reqArr[0]);
        if (reqArr.length > 1) {
            buildBody(reqArr[1]);
        }
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

    private void doServlet(HttpRequest request, HttpResponse response) throws IllegalAccessException, InstantiationException {
        String url = request.getUrl();
        Class<HttpServlet> httpServlet = ServletContainer.get(url);
        if (httpServlet != null) {
            httpServlet.newInstance().doService(request, response);
        }
    }

    private void buildReponse() {
        response.setVersion(Constants.HTTP_V1_1_VERSION);
        response.setStatusCode(Constants.STATUS_CODE_200);
        response.setMessage(Constants.STATUS_MESSAGE_OK);
        response.setDate(new Date());
        response.setContentLength(response.getBody().getBytes().length);

        buildCookie();
    }

    protected abstract void sendReponse() throws IOException;

    protected abstract void close() throws IOException;

    /**  other functions  */
    protected abstract String buildRequestStr() throws IOException;

    private void buildHeaders(String headStr) {
        String[] headers = headStr.split("\r\n");

        String statusStr = headers[0];
        String[] status = statusStr.split("\\s+");
        request.setMethod(HttpRequest.Method.valueOf(status[0]));
        request.setUrl(status[1]);
        request.setVersion(status[2]);

        Arrays.stream(headers)
                .skip(1)
                .forEach(headerStr -> {
                    int colonIndex = headerStr.indexOf(Constants.COLON);
                    String key = headerStr.substring(0, colonIndex).trim();
                    String val = headerStr.substring(colonIndex + 1).trim();
                    request.getHeaders().put(key, val);
                });
    }

    private void buildBody(String bodyStr) {
        request.setBody(bodyStr.getBytes());
        if(Constants.APPLICATION_FORM_URLENCODED.equals(request.getHeaders().get(Constants.CONTENT_TYPE))){
            // build params
            Map<String, List<Object>> params =new HashMap<>();
            String[] paramArr= bodyStr.split(Constants.AMPERSAND);
            for(String paramStr : paramArr){
                int index = paramStr.indexOf(Constants.EQUAL);
                List<Object> vals = new ArrayList<>();
                vals.add(paramStr.substring(index + 1));
                params.put(paramStr.substring(0, index), vals);
            }
        }
    }

    private void buildCookie(){
        if(request.getHeaders().containsKey("Cookie")){
            response.setSetCookie(request.getHeaders().get("Cookie"));
        }else{
            response.setSetCookie(UUID.randomUUID().toString());
        }
    }

}
