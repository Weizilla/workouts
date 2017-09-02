package com.weizilla.workouts.store;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.weizilla.workouts.entity.Activity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MemoryGarminStore implements GarminStore {
    private final Map<Long, Activity> byId = new HashMap<>();
    private final ListMultimap<LocalDate, Activity> byDate = ArrayListMultimap.create();

    @Override
    public void add(Activity activity) {
        byId.put(activity.getId(), activity);
        byDate.put(activity.getDate(), activity);
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
        return byId.values().stream()
            .sorted(Comparator.comparing(Activity::getDate))
            .collect(Collectors.toList());
    }
}
