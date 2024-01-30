package com.cyfrania.logback;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class SlackChatApiTest {
    private final MockSlackServer server = new MockSlackServer();
    private final SlackChatApi chatApi = new SlackChatApi();

    @Before
    public void setUp() throws Exception {
        server.start();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void getApiUrl_defasult() {
        assertEquals(SlackChatApi.DEFAULT_API_URL, chatApi.apiUrl);
    }
    
    @Test
    public void append_simple() throws IOException {
        chatApi.setApiUrl(server.getUrl());
        chatApi.setToken("secret-token-xxx");
        chatApi.setChannel("channel one");

        chatApi.sendMessage("Hello, World!");
        
        MockSlackServer.RequestInfo request = server.getRequest(0);
        assertEquals("Bearer secret-token-xxx", request.headers.get("Authorization"));
        assertEquals("application/json", request.headers.get("Content-Type"));
        assertEquals("{\"channel\":\"channel one\",\"text\":\"Hello, World!\"}", request.body);
        assertEquals("Hello, World!", Json.readAsMap(request.body).get("text"));
    }
}
