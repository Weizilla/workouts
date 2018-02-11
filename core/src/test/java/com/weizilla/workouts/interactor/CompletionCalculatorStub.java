package com.weizilla.workouts.interactor;

import com.weizilla.distance.Distance;
import com.weizilla.workouts.entity.Completion;
import com.weizilla.workouts.entity.Goal;

import java.time.Duration;

public class CompletionCalculatorStub extends CompletionCalculator {
    private final Completion result;

    public CompletionCalculatorStub(Completion result) {
        this.result = result;
    }

    @Override
    public Completion calculate(Goal goal, Duration actualDuration, Distance actualDistance) {
        return result;
    }
}
