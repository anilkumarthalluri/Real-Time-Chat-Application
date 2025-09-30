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
        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl == null) {
            throw new IllegalStateException("DATABASE_URL environment variable is not set!");
        }

        // The DATABASE_URL from Render is in the format: postgres://user:password@host/database
        // The JDBC driver needs it in the format: jdbc:postgresql://host/database?user=user&password=password
        // This is a robust way to perform that conversion.

        URI dbUri = new URI(databaseUrl);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String jdbcUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + dbUri.getPort() + dbUri.getPath() + "?user=" + username + "&password=" + password;

        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .build();
    }
}
