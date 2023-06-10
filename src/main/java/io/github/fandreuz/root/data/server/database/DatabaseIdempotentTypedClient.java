package io.github.fandreuz.root.data.server.database;

import lombok.NonNull;

/**
 * Idempotent version of {@link DatabaseTypedClient}.
 *
 * @param <I>
 *            input type.
 * @param <O>
 *            output type.
 * @author fandreuz
 */
public interface DatabaseIdempotentTypedClient<I, O> extends DatabaseTypedClient<I, O> {

   /**
    * Create a new object in the database (idempotent).
    *
    * @param input
    *            the object to be inserted.
    */
   @Deprecated(since = "Prefer createOrUpdate")
   default void create(@NonNull I input) {
      createOrUpdate(input);
   }

   /**
    * Create a new object in the database (idempotent).
    *
    * @param input
    *            the object to be inserted.
    */
   void createOrUpdate(@NonNull I input);
}
