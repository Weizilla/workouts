package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.store.GoalStore;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Singleton
public class GetGoals {
    private final GoalStore store;

    @Inject
    public GetGoals(GoalStore store) {
        this.store = store;
    }

    public Goal get(UUID id) {
        return store.get(id);
    }

    public List<Goal> get(LocalDate date) {
        return store.get(date);
    }

    public List<Goal> getAll() {
        return store.getAll();
    }
}
