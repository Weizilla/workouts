package com.weizilla.workouts.memory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.weizilla.garmin.entity.Activity;
import com.weizilla.workouts.store.GarminStore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MemoryGarminStore implements GarminStore {
    private final ListMultimap<LocalDate, Activity> byDate = ArrayListMultimap.create();

    @Override
    public void addAll(Collection<Activity> activities) {
        activities.stream()
            .forEach(a -> byDate.put(a.getStart().toLocalDate(), a));
    }

    @Override
    public List<Activity> get(LocalDate date) {
        return byDate.get(date);
    }

    @Override
    public List<Activity> getAll() {
        return new ArrayList<>(byDate.values());
    }
}
