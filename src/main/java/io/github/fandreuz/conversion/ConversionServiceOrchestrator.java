package io.github.fandreuz.conversion;

import io.github.fandreuz.model.DatasetType;

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
