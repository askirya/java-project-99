package hexlet.code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the Spring Boot application.
 */
@SpringBootApplication
public class AppApplication {

    /**
     * Starts the application.
     * @param args command-line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }
}
