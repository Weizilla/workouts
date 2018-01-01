package com.weizilla.workouts.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.weizilla.garmin.configuration.Credentials;
import com.weizilla.garmin.configuration.UrlBases;
import com.weizilla.workouts.store.GarminStore;
import com.weizilla.workouts.store.GoalStore;
import com.weizilla.workouts.store.RecordStore;

import javax.inject.Singleton;

public class WorkoutsModule extends AbstractModule {
    private final UrlBases urlBases;
    private final Credentials credentials;
    private final GarminStore garminStore;
    private final RecordStore recordStore;
    private final GoalStore goalStore;

    public WorkoutsModule(UrlBases urlBases, Credentials credentials,
            GarminStore garminStore, RecordStore recordStore, GoalStore goalStore) {
        this.urlBases = urlBases;
        this.credentials = credentials;
        this.garminStore = garminStore;
        this.recordStore = recordStore;
        this.goalStore = goalStore;
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

    @Singleton
    @Provides
    public RecordStore getRecordStore() {
        return recordStore;
    }

    @Singleton
    @Provides
    public GoalStore getGoalStore() {
        return goalStore;
    }
}
