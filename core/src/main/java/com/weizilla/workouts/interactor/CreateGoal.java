package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.store.GoalStore;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CreateGoal {
    private final GoalStore goalStore;

    @Inject
    public CreateGoal(GoalStore goalStore) {
        this.goalStore = goalStore;
    }

    public Goal create(Goal goal) {
        goalStore.add(goal);
        return goal;
    }
}
