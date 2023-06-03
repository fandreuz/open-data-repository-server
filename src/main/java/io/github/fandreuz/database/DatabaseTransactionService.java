package io.github.fandreuz.database;

/**
 * Enables transaction across different instances of {@link DatabaseTypeClient} sharing the same DB client.
 * <p>
 * Users of this class are responsible of making sure that the members participating the transaction are
 * using the same client instance.
 *
 * @author fandreuz
 */
public interface DatabaseTransactionService {

    /**
     * Start the transaction.
     */
    TransactionController start();
}
