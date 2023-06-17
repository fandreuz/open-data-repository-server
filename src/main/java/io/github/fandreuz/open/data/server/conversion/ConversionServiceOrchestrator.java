package io.github.fandreuz.open.data.server.conversion;

import io.github.fandreuz.open.data.server.model.dataset.DatasetType;
import lombok.NonNull;

/**
 * Interface to orchestrate multiple conversion services.
 *
 * @author fandreuz
 */
public interface ConversionServiceOrchestrator {

   /**
    * Get the appropriate conversion service for the given dataset type.
    *
    * @param datasetType
    *            dataset type.
    * @return the appropriate conversion service for the dataset type.
    */
   ConversionService getConversionService(@NonNull DatasetType datasetType);
}
