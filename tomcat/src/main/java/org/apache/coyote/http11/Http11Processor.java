package org.apache.coyote.http11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.Optional;
import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.exception.UncheckedServletException;
import nextstep.jwp.model.User;
import org.apache.coyote.Processor;
import org.apache.coyote.domain.FilePath;
import org.apache.coyote.domain.HttpRequest;
import org.apache.coyote.domain.MyHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Http11Processor implements Runnable, Processor {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

    private final Socket connection;

    public Http11Processor(final Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        process(connection);
    }

    @Override
    public void process(final Socket connection) {
        try (final InputStream inputStream = connection.getInputStream();
             final OutputStream outputStream = connection.getOutputStream();
             final BufferedReader inputBufferedReader = new BufferedReader(new InputStreamReader(inputStream));) {

            final String firstLine = inputBufferedReader.readLine();
            final HttpRequest httpRequest = HttpRequest.from(firstLine);
            final FilePath filePath = FilePath.from(httpRequest.getUri());

            if (httpRequest.getUri().contains("login?")) {
                Optional<User> user = InMemoryUserRepository.findByAccount(httpRequest.getQueryParam().get("account"));
                user.ifPresent(value -> log.info(value.toString()));
            }

            final MyHttpResponse httpResponse = MyHttpResponse.from(filePath);
            outputStream.write(httpResponse.getValue().getBytes());
            outputStream.flush();
        } catch (IOException | UncheckedServletException | URISyntaxException e) {
            log.error(e.getMessage(), e);
        }
    }
}
