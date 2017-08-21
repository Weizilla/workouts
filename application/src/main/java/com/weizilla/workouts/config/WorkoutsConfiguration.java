package com.weizilla.workouts.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.bundles.assets.AssetsBundleConfiguration;
import io.dropwizard.bundles.assets.AssetsConfiguration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class WorkoutsConfiguration extends Configuration implements AssetsBundleConfiguration {
    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    @JsonProperty
    private final AssetsConfiguration assets = AssetsConfiguration.builder().build();

    @Valid
    @NotNull
    @JsonProperty
    private GarminConfiguration garmin;

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory factory) {
        this.database = factory;
    }

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @Override
    public AssetsConfiguration getAssetsConfiguration() {
        return assets;
    }

    public GarminConfiguration getGarmin() {
        return garmin;
    }

    public void setGarmin(GarminConfiguration garmin) {
        this.garmin = garmin;
    }
}
