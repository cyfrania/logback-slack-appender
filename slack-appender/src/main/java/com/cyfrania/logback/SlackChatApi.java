package com.cyfrania.logback;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static ch.qos.logback.core.encoder.JsonEscapeUtil.jsonEscapeString;

public class SlackChatApi implements SlackSender {
    static final String DEFAULT_API_URL = "https://slack.com/api/chat.postMessage";
    
    String apiUrl = DEFAULT_API_URL;
    private String token;
    private String channel;

    void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int sendMessage(String text) throws IOException {
        return Http.post(new URL(apiUrl), getHeaders(), toJson(text));
    }

    private Map<String, String> getHeaders() {
        final Map<String, String> result = new HashMap<>();
        result.put("Authorization", "Bearer " + token);
        result.put("Content-Type", "application/json");
        return result;
    }

    private String toJson(String text) {
        return String.format("{\"channel\":\"%s\",\"text\":\"%s\"}", jsonEscapeString(channel), jsonEscapeString(text));
    }
}
