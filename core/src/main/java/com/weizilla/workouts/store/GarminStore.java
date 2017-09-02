package com.weizilla.workouts.store;

import com.weizilla.workouts.entity.Activity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface GarminStore {
    void add(Activity activity);
    void addAll(Collection<Activity> activities);
    Activity get(long id);
    List<Activity> get(LocalDate date);
    List<Activity> getAll();
}
