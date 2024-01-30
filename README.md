# Logback Slack Appender 

This is an Appender for [Logback](http://logback.qos.ch/) 
to send log messages and exceptions to a [Slack](https://slack.com/) channel.

It works with Logback 1.3.x (Java 8+) and Logback 1.4.x (Java 11+).
It does not have any external dependencies (besides Logback itself).

Slack channel to receive log messages is configured 
via [Incoming Webhook](https://api.slack.com/messaging/webhooks)
or via API token and channel ID.

## Usage example with Webhook

Logback configuration file `logback.xml`:

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration debug="true">
  <import class="ch.qos.logback.classic.encoder.PatternLayoutEncoder"/>
  <import class="ch.qos.logback.core.ConsoleAppender"/>
  <import class="ch.qos.logback.classic.AsyncAppender"/>
  <import class="ch.qos.logback.classic.filter.ThresholdFilter"/>
  <import class="ch.qos.logback.classic.PatternLayout"/>
  <import class="com.cyfrania.logback.SlackAppender"/>
  <import class="com.cyfrania.logback.SlackWebhook"/>

  <variable resource="logback.properties"/>

  <shutdownHook/>

  <appender name="STDOUT" class="ConsoleAppender">
    <encoder class="PatternLayoutEncoder">
      <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} -%kvp- %msg%n</pattern>
    </encoder>
  </appender>

  <appender name="SLACK" class="SlackAppender">
    <sender class="SlackWebhook">
      <webhookUrl>${webhookUrl}</webhookUrl>
    </sender>
    <layout class="PatternLayout">
      <pattern>%d{HH:mm:ss.SSS} [%thread] %level %logger -%kvp- %msg%n</pattern>
    </layout>
  </appender>

  <appender name="ASYNC_SLACK" class="AsyncAppender">
    <maxFlushTime>5000</maxFlushTime>
    <appender-ref ref="SLACK"/>
    <filter class="ThresholdFilter">
      <level>error</level>
    </filter>
  </appender>

  <root level="info">
    <appender-ref ref="STDOUT"/>
    <appender-ref ref="ASYNC_SLACK"/>
  </root>
</configuration>
```
The actual value for the Slack Webhook URL is taken from `logback.properties`. 

## Usage example with API token and channel ID

Logback configuration file `logback.xml`:

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration debug="true">
  <import class="ch.qos.logback.classic.encoder.PatternLayoutEncoder"/>
  <import class="ch.qos.logback.core.ConsoleAppender"/>
  <import class="ch.qos.logback.classic.AsyncAppender"/>
  <import class="ch.qos.logback.classic.filter.ThresholdFilter"/>
  <import class="ch.qos.logback.classic.PatternLayout"/>
  <import class="com.cyfrania.logback.SlackAppender"/>
  <import class="com.cyfrania.logback.SlackChatApi"/>

  <variable resource="logback.properties"/>

  <shutdownHook/>

  <appender name="STDOUT" class="ConsoleAppender">
    <encoder class="PatternLayoutEncoder">
      <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} -%kvp- %msg%n</pattern>
    </encoder>
  </appender>

  <appender name="SLACK" class="SlackAppender">
    <sender class="SlackChatApi">
      <token>${token}</token>
      <channel>${channel}</channel>
    </sender>
    <layout class="PatternLayout">
      <pattern>%d{HH:mm:ss.SSS} [%thread] %level %logger -%kvp- %msg%n</pattern>
    </layout>
  </appender>

  <appender name="ASYNC_SLACK" class="AsyncAppender">
    <maxFlushTime>5000</maxFlushTime>
    <appender-ref ref="SLACK"/>
    <filter class="ThresholdFilter">
      <level>error</level>
    </filter>
  </appender>

  <root level="info">
    <appender-ref ref="STDOUT"/>
    <appender-ref ref="ASYNC_SLACK"/>
  </root>
</configuration>
```
The actual value for the Slack API token and channel ID is taken from `logback.properties`. 
