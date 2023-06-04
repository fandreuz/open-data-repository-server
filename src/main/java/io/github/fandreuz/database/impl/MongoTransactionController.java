package io.github.fandreuz.database.impl;

import com.mongodb.client.ClientSession;
import io.github.fandreuz.database.TransactionController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link TransactionController}.
 *
 * @author fandreuz
 */
@RequiredArgsConstructor
@Slf4j
class MongoTransactionController implements TransactionController {

    private final ClientSession session;

    private boolean committed = false;

    @Override
    public synchronized void commit() {
        log.info("Committing the transaction");
        committed = true;
        session.commitTransaction();
        session.close();
    }

    @Override
    public synchronized void abort() {
        log.info("Aborting the transaction");
        session.abortTransaction();
        session.close();
    }

    @Override
    public void close() {
        synchronized (this) {
            if (!committed) {
                abort();
            }
        }
    }
}
