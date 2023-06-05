package io.github.fandreuz.root.data.server.conversion.impl;

import io.github.fandreuz.root.data.server.conversion.ConversionService;
import io.github.fandreuz.root.data.server.conversion.ConversionServiceOrchestrator;
import io.github.fandreuz.root.data.server.conversion.ConversionServiceUnavailableException;
import io.github.fandreuz.root.data.server.model.DatasetType;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.EnumMap;

/**
 * Implementation of {@link ConversionServiceOrchestrator}.
 *
 * @author fandreuz
 */
@Singleton
final class ConversionServiceOrchestratorImpl implements ConversionServiceOrchestrator {

   @Inject
   private CsvConversionService csvConversionService;

   @Inject
   private RootConversionService rootConversionService;

   private final EnumMap<DatasetType, ConversionService> conversionServices = new EnumMap<>(DatasetType.class);

   @PostConstruct
   void postConstruct() {
      conversionServices.put(DatasetType.CSV, csvConversionService);
      conversionServices.put(DatasetType.ROOT, rootConversionService);
   }

   @Override
   public ConversionService getConversionService(DatasetType datasetType) {
      if (!conversionServices.containsKey(datasetType)) {
         throw new ConversionServiceUnavailableException(datasetType);
      }
      return conversionServices.get(datasetType);
   }
}
