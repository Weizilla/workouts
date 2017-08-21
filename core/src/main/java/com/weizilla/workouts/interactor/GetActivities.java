package com.weizilla.workouts.interactor;

import com.weizilla.garmin.entity.Activity;
import com.weizilla.workouts.store.GarminStore;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDate;
import java.util.List;

@Singleton
public class GetActivities {
    private final GarminStore garminStore;

    @Inject
    public GetActivities(GarminStore garminStore) {
        this.garminStore = garminStore;
    }

    public List<Activity> get(LocalDate date) {
        return garminStore.get(date);
    }

    public List<Activity> getAll() {
        return garminStore.getAll();
    }
}
