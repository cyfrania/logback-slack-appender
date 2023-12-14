package com.cyfrania.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.JsonEscapeUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class SlackAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    private String webhookUrl;
    private Layout<ILoggingEvent> layout;

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public void setLayout(Layout<ILoggingEvent> layout) {
        this.layout = layout;
    }

    @Override
    public void start() {
        if (webhookUrl == null) {
            addError("No webhookUrl set for the appender named [" + name + "].");
        } else if (layout == null) {
            addError("No layout set for the appender named [" + name + "].");
        } else {
            super.start();
        }
    }

    @Override
    protected void append(ILoggingEvent event) {
        try {
            int responseCode = postToWebhook(transformToJson(event));
            if (responseCode != HttpURLConnection.HTTP_OK) {
                addError("Cannot post to Slack. Got HTTP Error " + responseCode);
            }
        } catch (Exception e) {
            addError("Cannot post to Slack.", e);
        }
    }

    private int postToWebhook(String text) throws IOException {
        return Http.post(new URL(webhookUrl), "application/json", text);
    }

    private String transformToJson(ILoggingEvent event) {
        final String text = layout.doLayout(event);
        return String.format("{\"text\":\"%s\"}", JsonEscapeUtil.jsonEscapeString(text));
    }
}
