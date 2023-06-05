package io.github.fandreuz.root.data.server.database.impl;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Singleton;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

/**
 * Setup database client properties from the environment configuration.
 *
 * @author fandreuz
 */
@Singleton
@Slf4j
final class MongoClientSetup {

   private static final String CONNECTION_STRING_KEY = "mongodb.uri";

   private final String databaseConnectionString;
   private MongoClient mongoClient;

   MongoClientSetup() {
      log.info("Reading MongoDB connection string from environment ('{}') ...", CONNECTION_STRING_KEY);
      databaseConnectionString = System.getenv(CONNECTION_STRING_KEY);
      log.info("{}={}", CONNECTION_STRING_KEY, databaseConnectionString);
   }

   MongoClient getMongoClient() {
      if (mongoClient == null) {
         MongoClientSettings settings = MongoClientSettings.builder() //
               .applyConnectionString(new ConnectionString(databaseConnectionString)) //
               .codecRegistry(setupCodecRegistry()) //
               .build();
         mongoClient = MongoClients.create(settings);

         log.info("MongoClient instance created");
         log.info("Databases in the cluster: {}", mongoClient.listDatabaseNames().into(new ArrayList<>()));
      }
      return mongoClient;
   }

   private static CodecRegistry setupCodecRegistry() {
      CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder() //
            .automatic(true) //
            .build() //
      );
      return CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
   }

   @PreDestroy
   void cleanUp() {
      if (mongoClient != null) {
         mongoClient.close();
      }
   }
}
