package com.weizilla.workouts.store;

import com.weizilla.workouts.entity.Plan;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PlanStore {
    void add(Plan plan);
    Plan get(UUID id);
    List<Plan> get(LocalDate date);
    void update(Plan plan);
    void delete(UUID id);
}
