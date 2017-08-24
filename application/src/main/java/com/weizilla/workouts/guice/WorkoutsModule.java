package com.weizilla.workouts.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.weizilla.garmin.configuration.Credentials;
import com.weizilla.garmin.configuration.UrlBases;
import com.weizilla.workouts.store.GarminStore;

import javax.inject.Singleton;

public class WorkoutsModule extends AbstractModule {
    private final UrlBases urlBases;
    private final Credentials credentials;
    private final GarminStore garminStore;

    public WorkoutsModule(UrlBases urlBases, Credentials credentials,
            GarminStore garminStore) {
        this.urlBases = urlBases;
        this.credentials = credentials;
        this.garminStore = garminStore;
    }

    @Override
    protected void configure() {

    }

    @Singleton
    @Provides
    public UrlBases getBases() {
        return urlBases;
    }

    @Singleton
    @Provides
    public Credentials getCredentials() {
        return credentials;
    }

    @Singleton
    @Provides
    public GarminStore getGarminStore() {
        return garminStore;
    }
}
