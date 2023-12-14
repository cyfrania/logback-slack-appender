package com.cyfrania.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.status.Status;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SlackAppenderTest {
    private final MockSlackServer server = new MockSlackServer();
    private final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    private final SlackAppender appender = new SlackAppender();

    @Before
    public void setUp() throws Exception {
        appender.setContext(loggerContext);
        appender.setName("SLACK");
        appender.setWebhookUrl(server.getUrl());
        appender.setLayout(createPatternLayout("%msg"));

        server.start();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void start_ok() {
        assertFalse(appender.isStarted());
        appender.start();
        assertTrue(appender.isStarted());
    }

    @Test
    public void start_webhookUrlNotSet() {
        assertFalse(appender.isStarted());
        appender.setWebhookUrl(null);

        appender.start();

        assertFalse(appender.isStarted());
        assertEquals("No webhookUrl set for the appender named [SLACK].", getLastStatus().getMessage());
    }

    @Test
    public void start_layoutNotSet() {
        assertFalse(appender.isStarted());
        appender.setLayout(null);

        appender.start();

        assertFalse(appender.isStarted());
        assertEquals("No layout set for the appender named [SLACK].", getLastStatus().getMessage());
    }

    @Test
    public void append_simple() {
        appender.append(createLoggingEvent("Hello, World!"));

        String request = server.getRequest(0);
        assertEquals("{\"text\":\"Hello, World!\"}", request);
        assertEquals("Hello, World!", Json.readAsMap(request).get("text"));
    }

    @Test
    public void append_specialSymbol() {
        appender.append(createLoggingEvent("one \" quote"));

        assertEquals("one \" quote", Json.readAsMap(server.getRequest(0)).get("text"));
    }

    @Test
    public void append_layout() {
        appender.setLayout(createPatternLayout("%d{yyyy-MM-dd HH:mm:ss} [%thread] %level %logger - %msg"));

        LoggingEvent event = new LoggingEvent();
        event.setLoggerContext(loggerContext);
        event.setLoggerContextRemoteView(loggerContext.getLoggerContextRemoteView());
        event.setInstant(LocalDateTime.of(2023, 11, 3, 18, 10, 0).atZone(ZoneId.systemDefault()).toInstant());
        event.setThreadName("main");
        event.setLoggerName("pumpkin");
        event.setLevel(Level.INFO);
        event.setMessage("Hello, World!");

        appender.append(event);

        String expected = "2023-11-03 18:10:00 [main] INFO pumpkin - Hello, World!";
        assertEquals(expected, Json.readAsMap(server.getRequest(0)).get("text"));
    }

    @Test
    public void append_exception() {
        appender.setLayout(null);

        appender.append(createLoggingEvent("one"));

        Status status = getLastStatus();
        assertEquals("Cannot post to Slack.", status.getMessage());
        assertTrue(status.getThrowable() instanceof NullPointerException);  // layout is null
    }

    @Test
    public void append_httpError403() {
        server.responseStatus = 403;

        appender.append(createLoggingEvent("one"));

        assertEquals("Cannot post to Slack. Got HTTP Error 403", getLastStatus().getMessage());
    }

    private static LoggingEvent createLoggingEvent(String message) {
        LoggingEvent event = new LoggingEvent();
        event.setMessage(message);
        return event;
    }

    private PatternLayout createPatternLayout(String pattern) {
        PatternLayout layout = new PatternLayout();
        layout.setContext(loggerContext);
        layout.setPattern(pattern);
        layout.start();
        return layout;
    }

    private Status getLastStatus() {
        List<Status> list = loggerContext.getStatusManager().getCopyOfStatusList();
        return list.get(list.size() - 1);
    }
}
