package io.github.fandreuz.open.data.server.database;

import java.util.SortedMap;
import java.util.SortedSet;

import lombok.NonNull;

/**
 * Interface for database clients for which column extraction is supported.
 *
 * @param <I>
 *            input type.
 * @param <O>
 *            output type.
 * @author fandreuz
 */
public interface ExtractibleDatabaseTypedClient<I, O> extends DatabaseTypedClient<I, O> {

   /**
    * Get the content of the given column.
    *
    * @param id
    *            identifier of the entity.
    * @param columnName
    *            name of the column to be extracted.
    * @return the content of the column for the given ID if found.
    */
   SortedMap<String, String> getColumn(@NonNull String id, @NonNull String columnName);

   /**
    * Get the IDS of the entries which satisfy the query.
    *
    * @param id
    *            identifier of the entity.
    * @param query
    *            query to be matched.
    * @return the IDs satisfying the query.
    */
   SortedSet<String> getIdsWhere(@NonNull String id, @NonNull String query);
}
