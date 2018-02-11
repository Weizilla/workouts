package com.weizilla.workouts.interactor;

import com.weizilla.distance.Distance;
import com.weizilla.workouts.entity.Completion;

import java.time.Duration;
import java.util.Optional;

public class CompletionCalculatorStub extends CompletionCalculator {
    private final Completion result;

    public CompletionCalculatorStub(Completion result) {
        this.result = result;
    }

    @Override
    public Completion calculate(Optional<Duration> goalDuration, Optional<Distance> goalDistance,
            Optional<Duration> actualDuration, Optional<Distance> actualDistance) {
        return result;
    }
}
