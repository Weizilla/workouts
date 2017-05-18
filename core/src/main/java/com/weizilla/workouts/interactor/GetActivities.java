package com.weizilla.workouts.interactor;

import com.weizilla.garmin.entity.Activity;
import com.weizilla.workouts.store.GarminStore;

import java.time.LocalDate;
import java.util.List;

public class GetActivities {
    private final GarminStore garminStore;

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
