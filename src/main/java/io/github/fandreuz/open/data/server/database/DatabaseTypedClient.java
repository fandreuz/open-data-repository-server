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
   O getEntry(@NonNull String id);

   /**
    * Get the IDS of the entries which satisfy the query.
    *
    * @param query
    *            query to be matched.
    * @return the IDs satisfying the query.
    */
   Set<O> getEntriesMatching(@NonNull String query);
}
