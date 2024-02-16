package ru.kpfu.itis.gr201.ponomarev.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpClientImpl implements HttpClient {

    private final String authKey;

    public HttpClientImpl() {
        authKey = null;
    }

    public HttpClientImpl(String authKey) {
        this.authKey = authKey;
    }

    @Override
    public String get(String url, Map<String, Object> params) throws IOException {
        url += mapToParams(params);

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

        configRead(conn, "GET");

        String content = readFromConnection(conn);
        conn.disconnect();

        return content;
    }

    @Override
    public String post(String url, Map<String, Object> params) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

        configReadWrite(conn, "POST");

        String json = jsonify(params);
        writeToConnection(conn, json);

        String content = readFromConnection(conn);
        conn.disconnect();

        return content;
    }

    @Override
    public String put(String url, Map<String, Object> params) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

        configReadWrite(conn, "PUT");

        String json = jsonify(params);
        writeToConnection(conn, json);

        String content = readFromConnection(conn);
        conn.disconnect();

        return content;
    }

    @Override
    public String delete(String url, Map<String, Object> params) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

        configReadWrite(conn, "DELETE");

        String json = jsonify(params);
        writeToConnection(conn, json);

        String content = readFromConnection(conn);
        conn.disconnect();

        return content;
    }

    private void configRead(HttpURLConnection conn, String method) throws IOException {
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        if (authKey != null) {
            conn.setRequestProperty("Authorization", "Bearer " + authKey);
        }
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
    }

    private void configReadWrite(HttpURLConnection conn, String method) throws IOException {
        configRead(conn, method);
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
    }

    private String readFromConnection(HttpURLConnection conn) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String input;
            while ((input = br.readLine()) != null) {
                content.append(input);
            }
        }
        return content.toString();
    }

    private void writeToConnection(HttpURLConnection conn, String body) throws IOException {
        try (OutputStream out = conn.getOutputStream()) {
            byte[] input = body.getBytes(StandardCharsets.UTF_8);
            out.write(input);
        }
    }

    private String mapToParams(Map<String, Object> map) {
        if (map != null && !map.isEmpty()) {
            StringBuilder urlParams = new StringBuilder("?");
            for (Map.Entry<String, Object> e : map.entrySet()) {
                if (e.getKey() == null) throw new IllegalArgumentException("Map can't have null keys.");
                urlParams.append(e.getKey()).append("=").append(e.getValue()).append("&");
            }
            return urlParams.deleteCharAt(urlParams.length() - 1).toString();
        }

        return "";
    }

    private String jsonify(Object obj) {
        if (obj == null) return "";

        if (obj instanceof Number || obj instanceof Boolean) {
            return obj.toString();
        } else if (obj instanceof Map<?, ?> map) {
            if (map.isEmpty()) return "{}";
            StringBuilder sb = new StringBuilder("{");
            for (Map.Entry<?, ?> e : map.entrySet()) {
                if (e.getKey() == null) throw new IllegalArgumentException("Map can't have null keys.");
                sb.append('"').append(e.getKey()).append("\":").append(jsonify(e.getValue())).append(',');
            }
            return sb.deleteCharAt(sb.length() - 1).append('}').toString();
        } else if (obj instanceof Iterable<?> iter) {
            boolean isEmpty = true;
            StringBuilder sb = new StringBuilder("[");
            for (Object o : iter) {
                sb.append(jsonify(o)).append(',');
                isEmpty = false;
            }
            if (!isEmpty) sb.deleteCharAt(sb.length() - 1);
            return sb.append(']').toString();
        } else {
            return '"' + obj.toString() + '"';
        }
    }
}
