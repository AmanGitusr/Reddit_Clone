package com.redditclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application entry point that boots the Reddit clone REST API.
 */
@SpringBootApplication
public class RedditCloneApplication {
    /**
     * Starts the embedded web server and initializes Spring-managed modules.
     */
    public static void main(String[] args) {
        SpringApplication.run(RedditCloneApplication.class, args);
    }
}
