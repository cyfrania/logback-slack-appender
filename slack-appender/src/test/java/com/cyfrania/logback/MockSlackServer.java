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
import java.util.List;

public class MockSlackServer {
    private final int port = 8888;
    private final Server server = new Server(port);
    private final List<String> requests = new ArrayList<>();

    int responseStatus = HttpServletResponse.SC_OK;

    public MockSlackServer() {
        server.setHandler(new AbstractHandler() {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                requests.add(IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8));
                response.setStatus(responseStatus);
                baseRequest.setHandled(true);
            }
        });
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

    public String getRequest(int index) {
        return requests.get(index);
    }
}
