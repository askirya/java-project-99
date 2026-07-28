package hexlet.code.config;

import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers JsonNullable support for PATCH/update DTOs.
 * Avoids Spring Boot jackson customizer types so Hexlet checks
 * stay compatible across Spring Boot 3 and 4.
 */
@Configuration
public class JacksonConfig {

    /**
     * JsonNullable Jackson module (auto-registered by Spring Boot).
     * @return module
     */
    @Bean
    public JsonNullableModule jsonNullableModule() {
        return new JsonNullableModule();
    }
}
