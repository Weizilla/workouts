package com.weizilla.workouts.memory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.weizilla.garmin.entity.Activity;
import com.weizilla.workouts.store.GarminStore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryGarminStore implements GarminStore {
    private final ListMultimap<LocalDate, Activity> byDate = ArrayListMultimap.create();
    private final Map<Long, Activity> byId = new HashMap<>();

    @Override
    public void add(Activity activity) {
        byDate.put(activity.getStart().toLocalDate(), activity);
        byId.put(activity.getId(), activity);
    }

    @Override
    public void addAll(Collection<Activity> activities) {
        activities.forEach(this::add);
    }

    @Override
    public Activity get(long id) {
        return byId.get(id);
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
