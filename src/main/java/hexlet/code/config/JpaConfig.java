package hexlet.code.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA-related configuration.
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
