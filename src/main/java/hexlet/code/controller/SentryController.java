package hexlet.code.controller;

import io.sentry.Sentry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint to verify Sentry error reporting.
 */
@RestController
public class SentryController {

    /**
     * Sends a test exception to Sentry.
     * @return confirmation message
     */
    @GetMapping("/sentry-debug")
    public String sentryDebug() {
        try {
            throw new Exception("This is a test.");
        } catch (Exception e) {
            Sentry.captureException(e);
        }
        return "Sentry test event sent";
    }
}
