package io.github.fandreuz.database.impl;

import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Setup database client properties from the environment configuration.
 *
 * @author fandreuz
 */
@Singleton
@Slf4j
class MongoClientSetup {

    private static final String CONNECTION_STRING_KEY = "mongodb.uri";

    @Getter
    private final String databaseConnectionString;

    public MongoClientSetup() {
        log.info("Reading MongoDB connection string from environment ('{}') ...", CONNECTION_STRING_KEY);
        databaseConnectionString = System.getenv(CONNECTION_STRING_KEY);
        log.info("{}={}", CONNECTION_STRING_KEY, databaseConnectionString);
    }
}
