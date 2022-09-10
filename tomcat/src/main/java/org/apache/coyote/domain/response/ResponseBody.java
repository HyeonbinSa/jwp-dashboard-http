package org.apache.coyote.domain.response;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResponseBody {

    private static final String HOME_URI = "/";
    private static final String FILE_PATH_PREFIX = "static";
    private static final String HOME_MESSAGE = "Hello world!";

    private final String value;

    private ResponseBody(String value) {
        this.value = value;
    }

    public static ResponseBody from(String filePath) throws URISyntaxException, IOException {
        return new ResponseBody(getResponseBody(filePath));
    }

    private static String getResponseBody(String uriPath) throws URISyntaxException, IOException {
        if (uriPath.equals(HOME_URI)) {
            return HOME_MESSAGE;
        }
        String fileName = FILE_PATH_PREFIX + uriPath;
        final URL resource = ResponseBody.class.getClassLoader().getResource(fileName);
        final File file = Paths.get(resource.toURI()).toFile();
        return new String(Files.readAllBytes(file.toPath()));
    }

    public String getValue() {
        return value;
    }

    public String getResponse() {
        return "Content-Length: " + getValue().getBytes().length + " " + "\r\n"
                + "\r\n"
                + getValue();
    }
}
