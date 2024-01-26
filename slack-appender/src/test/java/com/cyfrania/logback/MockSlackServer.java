package com.cyfrania.logback;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockSlackServer {
    private final int port = 8888;
    private final Server server = new Server(port);
    private final List<RequestInfo> requests = new ArrayList<>();

    int responseStatus = HttpServletResponse.SC_OK;

    public MockSlackServer() {
        server.setHandler(new AbstractHandler() {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                requests.add(getRequestInfo(request));
                response.setStatus(responseStatus);
                baseRequest.setHandled(true);
            }
        });
    }

    private RequestInfo getRequestInfo(HttpServletRequest request) throws IOException {
        Map<String, String> headers = getHeaders(request);
        String body = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
        return new RequestInfo(headers, body);
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();
        for (String name : Collections.list(request.getHeaderNames())) {
            result.put(name, request.getHeader(name));
        }
        return result;
    }

    public void start() throws Exception {
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }

    public String getUrl() {
        return "http://localhost:" + port + "/";
    }

    public RequestInfo getRequest(int index) {
        return requests.get(index);
    }

    public static class RequestInfo {
        public RequestInfo(Map<String, String> headers, String body) {
            this.headers = headers;
            this.body = body;
        }

        public Map<String, String> headers;
        public String body;
    }
}
