package org.srangelito.autoparts;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigChecker {
    @Value ("${DB_USERNAME}")
    private String databaseUsername;
    @Value ("${DB_PASSWORD}")
    private String databasePassword;
    @Value ("${DB_URL}")
    private String databaseUrl;

    @PostConstruct
    public void init () {
        System.out.println("=============================================================================================================================================================================================================================================================================");
        System.out.println("Here are the values of the environment variables used in your Spring application's configuration:");
        System.out.println("Database Username: " + databaseUsername);
        System.out.println("Database Password: " + databasePassword);
        System.out.println("Database Url: " + databaseUrl);
        System.out.println("=============================================================================================================================================================================================================================================================================");
    }
}
