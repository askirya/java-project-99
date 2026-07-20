package hexlet.code.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Production datasource configuration for Render PostgreSQL.
 */
@Configuration
@Profile("production")
public class ProductionDataSourceConfig {

    /**
     * Builds a DataSource from DATABASE_URL (postgres://...) provided by Render.
     * @return configured DataSource
     * @throws URISyntaxException if DATABASE_URL is invalid
     */
    @Bean
    public DataSource dataSource() throws URISyntaxException {
        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl == null || databaseUrl.isBlank()) {
            throw new IllegalStateException("DATABASE_URL environment variable is not set");
        }

        URI dbUri = new URI(databaseUrl);
        String[] userInfo = dbUri.getUserInfo().split(":", 2);
        String username = userInfo[0];
        String password = userInfo.length > 1 ? userInfo[1] : "";
        String jdbcUrl = "jdbc:postgresql://" + dbUri.getHost()
                + ":" + dbUri.getPort()
                + dbUri.getPath();

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName("org.postgresql.Driver");
        return dataSource;
    }
}
