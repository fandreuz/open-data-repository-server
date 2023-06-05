package io.github.fandreuz.root.data.server.database;

/**
 * Enables transaction across different instances of {@link DatabaseTypedClient}
 * sharing the same DB client.
 *
 * <p>
 * Users of this class are responsible of making sure that the members
 * participating the transaction are using the same client instance.
 *
 * <p>
 * Users are also responsible for making sure that the values provided by this
 * interface are used in a thread-safe manner, we recommend checking also the
 * documentation for the corresponding classes.
 *
 * @author fandreuz
 */
public interface DatabaseTransactionService {

   /** Start the transaction. */
   TransactionController start();
}
