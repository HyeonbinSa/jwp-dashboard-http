package org.apache.coyote.domain.response.statusline;

import java.util.Arrays;
import org.apache.coyote.domain.response.Header;

public enum ContentType implements Header {
    HTML(".html", "text/html"),
    CSS(".css", "text/css"),
    JS(".js", "application/x-javascript"),
    ICO(".ico", "image/x-icon");

    private final String extension;
    private final String type;

    ContentType(String extension, String type) {
        this.extension = extension;
        this.type = type;
    }

    public static ContentType from(String filePath) {
        return Arrays.stream(ContentType.values())
                .filter(contentType -> filePath.contains(contentType.getExtension()))
                .findFirst().orElse(ContentType.HTML);
    }

    public String getExtension() {
        return extension;
    }

    public String getType() {
        return type;
    }

    @Override
    public String getHeader() {
        return "Content-Type: " + getType() + ";charset=utf-8 " + "\r\n";
    }
}
