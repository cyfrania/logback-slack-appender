package com.cyfrania.logback;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class SlackWebhookTest {
    private final MockSlackServer server = new MockSlackServer();
    private final SlackWebhook webhook = new SlackWebhook();

    @Before
    public void setUp() throws Exception {
        webhook.setWebhookUrl(server.getUrl());
        server.start();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }
    
    @Test
    public void append_simple() throws IOException {
        webhook.sendMessage("Hello, World!");
        
        MockSlackServer.RequestInfo request = server.getRequest(0);
        assertEquals("application/json", request.headers.get("Content-Type"));
        assertEquals("{\"text\":\"Hello, World!\"}", request.body);
        assertEquals("Hello, World!", Json.readAsMap(request.body).get("text"));
    }
}
