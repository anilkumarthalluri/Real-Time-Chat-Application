package com.chat.Real_Time.Chat.Application.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@Profile("!local") // This ensures this configuration is ONLY used when not running locally
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() throws URISyntaxException {
        // Render provides the database connection URL in this environment variable.
        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl == null) {
            throw new IllegalStateException("DATABASE_URL environment variable is not set!");
        }

        URI dbUri = new URI(databaseUrl);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        // Construct the proper JDBC URL from the URI components.
        String jdbcUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .build();
    }
}
