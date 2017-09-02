package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.store.GoalStore;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UpdateGoal {
    private final GoalStore store;

    @Inject
    public UpdateGoal(GoalStore store) {
        this.store = store;
    }

    public void updatePlan(Goal goal) {
        store.update(goal);
    }
}
