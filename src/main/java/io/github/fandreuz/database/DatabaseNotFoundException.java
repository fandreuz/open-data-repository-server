package io.github.fandreuz.database;

/**
 * Thrown when an entity is not found in the database.
 *
 * @author fandreuz
 */
public class DatabaseNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 0L;

    public DatabaseNotFoundException(String message) {
        super(message);
    }

    public DatabaseNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
