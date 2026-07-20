package hexlet.code.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller that returns a welcome message.
 */
@RestController
public class WelcomeController {

    /**
     * Handles GET requests to /welcome.
     * @return welcome message
     */
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to Spring";
    }
}
