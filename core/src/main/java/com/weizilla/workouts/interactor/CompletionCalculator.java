package com.weizilla.workouts.interactor;

import com.weizilla.distance.Distance;
import com.weizilla.workouts.entity.Completion;
import com.weizilla.workouts.entity.Goal;

import java.time.Duration;
import java.time.LocalDate;

public class CompletionCalculator {
    public Completion calculate(Goal goal, Duration actualDuration, Distance actualDistance) {
        if (goal == null || (goal.getDuration() == null && goal.getDistance() == null)) {
            return Completion.ALL;
        }

        if (goal.getDate().isAfter(LocalDate.now())) {
            return Completion.GOAL;
        }

        if (actualDuration == null && actualDistance == null) {
            return Completion.NONE;
        }

        Completion distanceCompletion = calcDistance(goal.getDistance(), actualDistance);
        Completion durationCompletion = calcDuration(goal.getDuration(), actualDuration);
        if (distanceCompletion == durationCompletion) {
            return distanceCompletion;
        } else {
            return Completion.SOME;
        }
    }

    private Completion calcDistance(Distance goalDistance, Distance actualDistance) {
        if (goalDistance == null) {
            return Completion.ALL;
        }

        if (actualDistance == null) {
            return Completion.NONE;
        }

        if (goalDistance.equals(actualDistance) || actualDistance.getDistanceMeter() > goalDistance.getDistanceMeter()) {
            return Completion.ALL;
        }

        return Completion.SOME;
    }

    private Completion calcDuration(Duration goalDuration, Duration actualDuration) {
        if (goalDuration == null) {
            return Completion.ALL;
        }

        if (actualDuration == null) {
            return Completion.NONE;
        }

        if (goalDuration.equals(actualDuration) || actualDuration.getSeconds() > goalDuration.getSeconds()) {
            return Completion.ALL;
        }

        return Completion.SOME;
    }
}
