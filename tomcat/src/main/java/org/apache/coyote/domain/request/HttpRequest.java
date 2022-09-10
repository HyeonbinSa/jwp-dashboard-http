package org.apache.coyote.domain.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import org.apache.coyote.domain.HttpCookie;
import org.apache.coyote.domain.request.requestline.HttpVersion;
import org.apache.coyote.domain.request.requestline.RequestLine;
import org.apache.coyote.session.Session;
import org.apache.coyote.session.SessionManager;

public class HttpRequest {

    public static final String SESSION_USER_KEY = "user";
    private final RequestLine requestLine;
    private final RequestHeader requestHeader;
    private final RequestBody requestBody;
    private final HttpCookie httpCookie;

    private HttpRequest(final RequestLine requestLine,
                        final RequestHeader requestHeader,
                        final RequestBody requestBody,
                        final HttpCookie httpCookie) {
        this.requestLine = requestLine;
        this.requestHeader = requestHeader;
        this.requestBody = requestBody;
        this.httpCookie = httpCookie;
    }

    public static HttpRequest from(BufferedReader inputReader) {
        try {
            String startLine = inputReader.readLine();
            final RequestLine requestLine = RequestLine.from(startLine);
            final RequestHeader requestHeader = RequestHeader.from(inputReader);
            final HttpCookie httpCookie = HttpCookie.from(requestHeader.getCookies());
            final RequestBody requestBody = RequestBody.of(inputReader, requestHeader.getContentLength());
            return new HttpRequest(requestLine, requestHeader, requestBody, httpCookie);
        } catch (IOException e) {
            throw new IllegalArgumentException("올바르지 않은 HttpRequest입니다.");
        }
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }

    public HttpCookie getHttpCookie() {
        return httpCookie;
    }

    public Session getSession() {
        if (httpCookie.hasJSESSIONID()) {
            return SessionManager.findSession(httpCookie.getJSESSIONID())
                    .orElse(new Session(UUID.randomUUID().toString()));
        }
        return new Session(UUID.randomUUID().toString());
    }

    public boolean checkSession() {
        Optional<Session> session = SessionManager.findSession(httpCookie.getJSESSIONID());
        if (session.isEmpty()) {
            return false;
        }
        Optional<Object> user = session.get().getAttribute(SESSION_USER_KEY);
        return user.isPresent();
    }

    public String getFilePath() {
        return requestLine.getFilePath();
    }

    public String getUri() {
        return requestLine.getUri();
    }

    public String getBodyValue(String key) {
        return requestBody.getValue(key);
    }

    public boolean hasJSESSIONID() {
        return httpCookie.hasJSESSIONID();
    }

    public HttpVersion getHttpVersion() {
        return requestLine.getHttpVersion();
    }

    public boolean isGet() {
        return requestLine.isGet();
    }
}
