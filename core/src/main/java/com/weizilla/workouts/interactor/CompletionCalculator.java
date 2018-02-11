package com.weizilla.workouts.interactor;

import com.weizilla.distance.Distance;
import com.weizilla.workouts.entity.Completion;

import java.time.Duration;
import java.util.Optional;

public class CompletionCalculator {
    public Completion calculate(Optional<Duration> goalDuration, Optional<Distance> goalDistance,
            Optional<Duration> actualDuration, Optional<Distance> actualDistance) {
        if (! goalDuration.isPresent() && ! goalDistance.isPresent()) {
            return Completion.ALL;
        }

        if (! actualDuration.isPresent() && ! actualDistance.isPresent()) {
            return Completion.NONE;
        }

        Completion distanceCompletion = calcCompletion(goalDistance, actualDistance);
        Completion durationCompletion = calcCompletion(goalDuration, actualDuration);
        if (distanceCompletion == durationCompletion) {
            return distanceCompletion;
        } else {
            return Completion.SOME;
        }
    }

    private static <T extends Comparable<T>> Completion calcCompletion(Optional<T> maybeGoal, Optional<T> maybeActual) {
        // No goal is ALL regardless
        // No actual with any goal is NONE
        // Goal and actual will be Some or ALL depending on goal > actual or goal <= actual
        return maybeGoal.map(goal ->
            maybeActual.map(actual ->
                goal.compareTo(actual) <= 0 ? Completion.ALL : Completion.SOME
            ).orElse(Completion.NONE)
        ).orElse(Completion.ALL);
    }
}
