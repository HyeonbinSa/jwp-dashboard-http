package org.apache.coyote.handler;

import java.util.List;
import org.apache.coyote.controller.Controller;
import org.apache.coyote.controller.LoginController;
import org.apache.coyote.controller.RegisterController;
import org.apache.coyote.controller.StaticFileController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestMapping {

    private static final Logger log = LoggerFactory.getLogger(RequestMapping.class);

    private static final List<Controller> controllers = List.of(new LoginController(), new RegisterController());

    private RequestMapping() {
    }

    public static Controller getController(final String uri) {
        log.info("[RequestMapping] {}, request mapping", uri);
        return controllers.stream()
                .filter(controller -> controller.handle(uri))
                .findFirst()
                .orElse(new StaticFileController());
    }
}
