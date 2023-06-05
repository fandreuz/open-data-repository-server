package io.github.fandreuz.root.data.server.model;

import java.util.Collection;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * Represents a dataset stored in the database.
 *
 * @author fandreuz
 */
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class StoredDataset {

   @Getter
   @EqualsAndHashCode.Include
   private final String id;

   private final SortedSet<String> columnNames;

   public StoredDataset(@NonNull String id, @NonNull Collection<String> columnNames) {
      this.id = id;
      this.columnNames = new TreeSet<>(columnNames);
   }

   public SortedSet<String> getColumnNames() {
      return Collections.unmodifiableSortedSet(columnNames);
   }
}
