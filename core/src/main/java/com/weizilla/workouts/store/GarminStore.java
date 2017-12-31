package com.weizilla.workouts.store;

import com.weizilla.workouts.entity.Activity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface GarminStore {
    void add(Activity value);

    void addAll(Collection<Activity> values);

    Activity get(Long id);

    List<Activity> get(LocalDate date);

    List<Activity> getAll();

    void update(Activity value);

    void delete(Long id);
}
