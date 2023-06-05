package io.github.fandreuz.root.data.server.conversion;

import io.github.fandreuz.root.data.server.model.DatasetType;

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
   ConversionService getConversionService(DatasetType datasetType);
}
