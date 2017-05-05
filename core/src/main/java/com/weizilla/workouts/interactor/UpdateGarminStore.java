package com.weizilla.workouts.interactor;

import com.weizilla.garmin.entity.Activity;
import com.weizilla.workouts.garmin.GarminSource;
import com.weizilla.workouts.store.GarminStore;

import java.util.List;

public class UpdateGarminStore {
    private final GarminSource garminSource;
    private final GarminStore garminStore;

    public UpdateGarminStore(GarminSource garminSource, GarminStore garminStore) {
        this.garminSource = garminSource;
        this.garminStore = garminStore;
    }

    public int update() {
        List<Activity> downloaded = garminSource.fetch();
        garminStore.addAll(downloaded);
        return downloaded.size();
    }
}
