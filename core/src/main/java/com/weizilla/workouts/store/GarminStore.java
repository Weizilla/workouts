package com.weizilla.workouts.store;

import com.weizilla.garmin.entity.Activity;

import java.util.Collection;

public interface GarminStore {
    void addAll(Collection<Activity> activities);
}
