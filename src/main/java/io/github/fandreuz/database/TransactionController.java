package io.github.fandreuz.database;

/**
 * Generic interface to control database transactions.
 * <p>
 * Implementations are expected to be thread-safe.
 *
 * @author fandreuz
 */
public interface TransactionController extends AutoCloseable {

    /**
     * Commit the transaction.
     */
    void commit();

    /**
     * Abort the transaction.
     */
    void abort();
}
