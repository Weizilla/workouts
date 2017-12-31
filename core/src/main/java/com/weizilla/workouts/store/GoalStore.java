package com.weizilla.workouts.store;

import com.weizilla.workouts.entity.Goal;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface GoalStore {
    void add(Goal value);

    void addAll(Collection<Goal> values);

    Goal get(UUID id);

    List<Goal> get(LocalDate date);

    List<Goal> getAll();

    void update(Goal value);

    void delete(UUID id);
}
