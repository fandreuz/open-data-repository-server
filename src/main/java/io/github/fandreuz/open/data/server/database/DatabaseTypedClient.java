package io.github.fandreuz.open.data.server.database;

import lombok.NonNull;

import java.util.Set;

/**
 * Interface for database clients.
 *
 * @param <I>
 *            input type.
 * @param <O>
 *            output type.
 * @author fandreuz
 */
public interface DatabaseTypedClient<I, O> {

   /**
    * Create a new object in the database.
    *
    * @param input
    *            the object to be inserted.
    */
   void create(@NonNull I input);

   /**
    * Find an existing object in the database.
    *
    * @param id
    *            ID of the object to be fetched.
    * @return the requested object if it exists.
    */
   O get(@NonNull String id);

   /**
    * Get all the objects stored in the database.
    *
    * @return a set containing all the objects in the database.
    */
   Set<O> getAll();
}
