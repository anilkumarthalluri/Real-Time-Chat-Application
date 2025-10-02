package com.chat.Real_Time.Chat.Application.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@Profile("prod") // This is the crucial fix: Only use this class in the 'prod' environment (Render)
public class JpaConfig {

    @Bean
    @Primary
    public DataSource dataSource() throws URISyntaxException {
        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl == null) {
            throw new IllegalStateException("DATABASE_URL environment variable is not set for prod profile.");
        }

        URI dbUri = new URI(databaseUrl);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        
        int port = dbUri.getPort();
        if (port == -1) {
            port = 5432; // Default PostgreSQL port
        }

        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + port + dbUri.getPath();

        return DataSourceBuilder.create()
                .url(dbUrl)
                .username(username)
                .password(password)
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}
