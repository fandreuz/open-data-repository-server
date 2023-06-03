package io.github.fandreuz.conversion.impl;

import io.github.fandreuz.conversion.ConversionService;
import io.github.fandreuz.conversion.ConversionServiceOrchestrator;
import io.github.fandreuz.conversion.ConversionServiceUnavailableException;
import io.github.fandreuz.model.DatasetType;
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

    private EnumMap<DatasetType, ConversionService> conversionServices;

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
