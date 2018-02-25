package com.weizilla.workouts.interactor;

import com.weizilla.distance.Distance;
import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.entity.ImmutableGoal;
import com.weizilla.workouts.store.GoalStore;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.util.Optional;

@Singleton
public class CreateGoal {
    private final GoalStore goalStore;

    @Inject
    public CreateGoal(GoalStore goalStore) {
        this.goalStore = goalStore;
    }

    public Goal create(Goal goal) {
        Optional<Duration> duration = goal.getDuration().filter(d -> d.getSeconds() > 0);
        Optional<Distance> distance = goal.getDistance().filter(d -> d.getDistanceMeter() > 0);
        Goal g = ImmutableGoal.copyOf(goal)
            .withDuration(duration)
            .withDistance(distance);
        goalStore.add(g);
        return g;
    }
}
