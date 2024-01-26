package com.cyfrania.logback;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

class Http {
    public static int post(URL url, String contentType, String text) throws IOException {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);

        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        int timeout = 30000;
        conn.setConnectTimeout(timeout);
        conn.setReadTimeout(timeout);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", contentType);
        conn.setFixedLengthStreamingMode(bytes.length);

        final OutputStream os = conn.getOutputStream();
        os.write(bytes);
        os.flush();
        os.close();

        return conn.getResponseCode();
    }
}
