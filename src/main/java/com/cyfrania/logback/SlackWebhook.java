package com.cyfrania.logback;

import ch.qos.logback.core.encoder.JsonEscapeUtil;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SlackWebhook implements SlackSender {
    private String webhookUrl;

    public SlackWebhook() {
    }

    public SlackWebhook(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    @Override
    public int sendMessage(String text) throws IOException {
        return Http.post(new URL(webhookUrl), getHeaders(), toJson(text));
    }

    private Map<String, String> getHeaders() {
        final Map<String, String> result = new HashMap<>();
        result.put("Content-Type", "application/json");
        return result;
    }

    private String toJson(String text) {
        return String.format("{\"text\":\"%s\"}", JsonEscapeUtil.jsonEscapeString(text));
    }
}
