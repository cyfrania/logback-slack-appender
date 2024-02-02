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

https://github.com/cyfrania/logback-slack-appender/blob/example-webhook/src/main/resources/logback.xml

The actual value for the Slack Webhook URL is taken from `logback.properties`. 

## Usage example with API token and channel ID

Logback configuration file `logback.xml`:

https://github.com/cyfrania/logback-slack-appender/blob/example-chatapi/src/main/resources/logback.xml

The actual value for the Slack API token and channel ID is taken from `logback.properties`. 
