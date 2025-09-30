package com.chat.Real_Time.Chat.Application.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class JpaConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        // Render provides the connection details as individual environment variables.
        // This is the most robust way to build the connection.
        String host = System.getenv("PGHOST");
        String port = System.getenv("PGPORT");
        String dbname = System.getenv("PGDATABASE");
        String username = System.getenv("PGUSER");
        String password = System.getenv("PGPASSWORD");

        if (host == null || port == null || dbname == null || username == null || password == null) {
            throw new IllegalStateException("Database environment variables are not fully set.");
        }

        String dbUrl = "jdbc:postgresql://" + host + ":" + port + "/" + dbname;

        return DataSourceBuilder.create()
                .url(dbUrl)
                .username(username)
                .password(password)
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}
