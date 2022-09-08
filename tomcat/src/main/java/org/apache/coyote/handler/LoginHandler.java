package org.apache.coyote.handler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;
import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;
import org.apache.coyote.domain.FilePath;
import org.apache.coyote.domain.request.HttpRequest;
import org.apache.coyote.domain.request.requestline.HttpMethod;
import org.apache.coyote.domain.response.HttpResponse;
import org.apache.coyote.domain.response.HttpStatusCode;
import org.apache.coyote.domain.response.RedirectUrl;
import org.apache.coyote.http11.Http11Processor;
import org.apache.coyote.session.Session;
import org.apache.coyote.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginHandler implements Handler {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

    @Override
    public HttpResponse run(HttpRequest httpRequest) throws URISyntaxException, IOException {
        final FilePath filePath = FilePath.from(httpRequest.getRequestLine().getPath().getPath());
        if (httpRequest.getHttpCookie().hasJSESSIONID()) {
            if (httpRequest.checkSession()) {
                return HttpResponse.from(httpRequest.getRequestLine().getHttpVersion(), filePath, HttpStatusCode.FOUND)
                        .addRedirectUrlHeader(RedirectUrl.from("/index.html"));
            }
        }
        if (httpRequest.getRequestLine().getHttpMethod().equals(HttpMethod.GET) && httpRequest.getRequestLine()
                .getPath().getQueryParam().isEmpty()) {
            return HttpResponse.from(httpRequest.getRequestLine().getHttpVersion(), filePath, HttpStatusCode.OK);
        }
        return login(httpRequest);
    }

    private static HttpResponse login(HttpRequest httpRequest) throws URISyntaxException, IOException {
        final FilePath filePath = FilePath.from(httpRequest.getRequestLine().getPath().getPath());
        Optional<User> user = InMemoryUserRepository.findByAccount(
                httpRequest.getRequestBody().getRequestBody().get("account"));
        if (user.isPresent()) {
            log.info(user.get().toString());
            if (user.get().checkPassword(httpRequest.getRequestBody().getRequestBody().get("password"))) {
                Session session = httpRequest.getSession();
                session.setAttribute("user", user);
                SessionManager.add(session);
                httpRequest.getHttpCookie().add(session);
                return HttpResponse.from(httpRequest.getRequestLine().getHttpVersion(), filePath, HttpStatusCode.FOUND)
                        .addRedirectUrlHeader(RedirectUrl.from("/index.html"))
                        .addSetCookieHeader(httpRequest.getHttpCookie());
            }
        }
        return HttpResponse.from(httpRequest.getRequestLine().getHttpVersion(), filePath, HttpStatusCode.FOUND)
                .addRedirectUrlHeader(RedirectUrl.from("/401.html"));
    }
}
