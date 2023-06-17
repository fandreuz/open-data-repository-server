package io.github.fandreuz.open.data.server.database;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import lombok.NonNull;

/**
 * Interface for database clients with restricted access to the entries.
 * <p>
 * This should be used rather than {@link DatabaseTypedClient} when the
 * collection contains lots of entries, or when the number of columns makes
 * unfeasible the extraction of set of entries without filters.
 *
 * @param <I>
 *            input type.
 * @author fandreuz
 */
public interface MonolithicDatabaseTypedClient<I> {

   /**
    * Create a new object in the database.
    *
    * @param input
    *            the object to be inserted.
    */
   void create(@NonNull I input);

   /**
    * Get the content of the given column.
    *
    * @param id
    *            identifier of the entity.
    * @param columnName
    *            name of the column to be extracted.
    * @return the content of the column for the given ID if found.
    */
   Map<String, String> getColumn(@NonNull String id, @NonNull String columnName);

   /**
    * Get the IDS of the entries which satisfy the query.
    *
    * @param id
    *            identifier of the entity.
    * @param query
    *            query to be matched.
    * @return the IDs satisfying the query.
    */
   Set<SortedMap<String, String>> getEntriesMatching(@NonNull String id, @NonNull String query);
}
