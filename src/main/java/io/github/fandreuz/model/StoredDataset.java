package io.github.fandreuz.model;

import java.util.Collections;
import java.util.SortedSet;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Represents a dataset stored in the database.
 *
 * @author fandreuz
 */
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public final class StoredDataset {

    @Getter
    private final String id;

    private final SortedSet<String> columnNames;

    public SortedSet<String> getColumnNames() {
        return Collections.unmodifiableSortedSet(columnNames);
    }
}
