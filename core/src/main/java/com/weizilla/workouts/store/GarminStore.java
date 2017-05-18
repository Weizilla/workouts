package com.weizilla.workouts.store;

import com.weizilla.garmin.entity.Activity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface GarminStore {
    void addAll(Collection<Activity> activities);
    List<Activity> get(LocalDate date);
    List<Activity> getAll();
}
