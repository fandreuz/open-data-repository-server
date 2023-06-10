package io.github.fandreuz.root.data.server.database.impl;

import com.mongodb.client.ClientSession;
import io.github.fandreuz.root.data.server.database.DatabaseTransactionService;
import io.github.fandreuz.root.data.server.database.TransactionController;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

/**
 * MongoDB implementation of {@link DatabaseTransactionService}.
 *
 * @author fandreuz
 */
@Slf4j
@Singleton
final class MongoTransactionService implements DatabaseTransactionService {

   @Inject
   private MongoClientSetup databaseClientSetup;

   @Override
   public TransactionController start() {
      ClientSession session = databaseClientSetup.getMongoClient().startSession();
      log.info("Transaction starting");
      session.startTransaction();
      return new MongoTransactionController(session);
   }
}
