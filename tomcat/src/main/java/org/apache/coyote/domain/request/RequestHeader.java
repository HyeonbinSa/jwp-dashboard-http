package org.apache.coyote.domain.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestHeader {

    private static final String HEADER_REGEX = ": ";
    private static final String EMPTY_HEADER = "";
    private static final int HEADER_KEY = 0;
    private static final int HEADER_VALUE = 1;
    private static final String CONTENT_LENGTH_KEY = "Content-Length";
    private static final String DEFAULT_CONTENT_LENGTH = "0";
    private static final String COOKIE_KEY = "Cookie";
    private static final String DEFAULT_COOKIE = "";

    private final Map<String, String> headers;

    private RequestHeader(Map<String, String> headers) {
        this.headers = headers;
    }

    public static RequestHeader from(BufferedReader inputReader) throws IOException {
        Map<String, String> requestHeader = new HashMap<>();
        String line = inputReader.readLine();
        while (inputReader.ready() && !line.equals(EMPTY_HEADER)) {
            String[] header = line.split(HEADER_REGEX);
            requestHeader.put(header[HEADER_KEY], header[HEADER_VALUE]);
            line = inputReader.readLine();
        }
        return new RequestHeader(requestHeader);
    }

    public int getContentLength() {
        return Integer.parseInt(headers.getOrDefault(CONTENT_LENGTH_KEY, DEFAULT_CONTENT_LENGTH));
    }

    public String getCookies() {
        return headers.getOrDefault(COOKIE_KEY, DEFAULT_COOKIE);
    }
}
