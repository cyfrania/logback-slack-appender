package com.cyfrania.logback;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

class Http {
    public static int post(URL url, Map<String, String> headers, String text) throws IOException {
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        int timeout = 30000;
        conn.setConnectTimeout(timeout);
        conn.setReadTimeout(timeout);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            conn.setRequestProperty(entry.getKey(), entry.getValue());
        }

        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        conn.setFixedLengthStreamingMode(bytes.length);

        final OutputStream os = conn.getOutputStream();
        os.write(bytes);
        os.flush();
        os.close();

        return conn.getResponseCode();
    }
}
