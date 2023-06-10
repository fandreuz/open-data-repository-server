package io.github.fandreuz.root.data.server.model.dataset;

import java.nio.file.Path;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * Catalyzer for importing a new dataset in the database.
 *
 * <p>
 * Contains critical information to finalize the import operation.
 *
 * @author fandreuz
 */
@AllArgsConstructor
@NonNull
@Getter
@ToString
public class DatasetCoordinates {

   private final String id;
   private final Path localFileLocation;
}
