package ru.kpfu.itis.gr201.ponomarev.http;

import java.io.IOException;
import java.util.Map;

public interface HttpClient {
    String get(String url, Map<String, Object> params) throws IOException;
    String post(String url, Map<String, Object> params) throws IOException;
    String put(String url, Map<String, Object> params) throws IOException;
    String delete(String url, Map<String, Object> params) throws IOException;
}
