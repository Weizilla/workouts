package com.weizilla.workouts.interactor;

import com.weizilla.workouts.store.GoalStore;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class DeleteGoal {
    private final GoalStore store;

    @Inject
    public DeleteGoal(GoalStore store) {
        this.store = store;
    }

    public UUID delete(UUID id) {
        store.delete(id);
        return id;
    }
}
