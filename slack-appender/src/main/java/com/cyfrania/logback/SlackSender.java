package com.cyfrania.logback;

import java.io.IOException;

public interface SlackSender {
    int sendMessage(String text) throws IOException;
}
