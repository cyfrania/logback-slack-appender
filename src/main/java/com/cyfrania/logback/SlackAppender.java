package com.cyfrania.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

import java.net.HttpURLConnection;

public class SlackAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    private SlackSender sender;
    private Layout<ILoggingEvent> layout;

    public void setSender(SlackSender sender) {
        this.sender = sender;
    }

    public void setLayout(Layout<ILoggingEvent> layout) {
        this.layout = layout;
    }

    @Override
    public void start() {
        if (sender == null) {
            addError("No sender set for the appender named [" + name + "].");
        } else if (layout == null) {
            addError("No layout set for the appender named [" + name + "].");
        } else {
            super.start();
        }
    }

    @Override
    protected void append(ILoggingEvent event) {
        try {
            final int responseCode = sender.sendMessage(layout.doLayout(event));
            if (responseCode != HttpURLConnection.HTTP_OK) {
                addError("Cannot post to Slack. Got HTTP Error " + responseCode);
            }
        } catch (Exception e) {
            addError("Cannot post to Slack.", e);
        }
    }
}
