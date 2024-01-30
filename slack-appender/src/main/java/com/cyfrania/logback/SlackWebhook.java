package com.cyfrania.logback;

import ch.qos.logback.core.encoder.JsonEscapeUtil;

import java.io.IOException;
import java.net.URL;

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
        return Http.post(new URL(webhookUrl), "application/json", toJson(text));
    }

    private String toJson(String text) {
        return String.format("{\"text\":\"%s\"}", JsonEscapeUtil.jsonEscapeString(text));
    }
}
