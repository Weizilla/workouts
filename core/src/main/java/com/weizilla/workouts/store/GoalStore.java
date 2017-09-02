package com.weizilla.workouts.store;

import com.weizilla.workouts.entity.Goal;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface GoalStore {
    void add(Goal goal);
    Goal get(UUID id);
    List<Goal> getAll();
    List<Goal> get(LocalDate date);
    void update(Goal goal);
    void delete(UUID id);
}
