package com.weizilla.workouts.interactor;

import com.weizilla.distance.Distance;
import com.weizilla.workouts.entity.Completion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class CompletionCalculatorParameterTest {
    private static final Distance DISTANCE = Distance.ofKilometers(1);
    private static final Distance DISTANCE_HALF = DISTANCE.dividedBy(2);
    private static final Duration DURATION = Duration.ofHours(1);
    private static final Duration DURATION_HALF = DURATION.dividedBy(2);
    private final CompletionCalculator calculator;
    private final Duration goalDuration;
    private final Distance goalDistance;
    private final Duration totalDuration;
    private final Distance totalDistance;
    private final Completion expected;

    public CompletionCalculatorParameterTest(Duration goalDuration,
            Distance goalDistance, Duration totalDuration, Distance totalDistance, Completion expected) {
        calculator = new CompletionCalculator();
        this.goalDuration = goalDuration;
        this.goalDistance = goalDistance;
        this.totalDuration = totalDuration;
        this.totalDistance = totalDistance;
        this.expected = expected;
    }

    @Test
    public void testCalculator() {
        Completion completion = calculator.calculate(Optional.ofNullable(goalDuration),
            Optional.ofNullable(goalDistance),
            Optional.ofNullable(totalDuration),
            Optional.ofNullable( totalDistance));
        assertThat(completion).isEqualTo(expected);
    }

    @Parameters(name = "{index}: Goal ({0},{1}) Target({2},{3}) = {4}")
    public static Object[][] parameters() {
        return new Object[][] {
            // no goal duration or distance means ALL completed
            new Object[]{null, null, null, null, Completion.ALL},
            new Object[]{null, null, DURATION, null, Completion.ALL},
            new Object[]{null, null, null, DISTANCE, Completion.ALL},
            new Object[]{null, null, DURATION, DISTANCE, Completion.ALL},
            // must match goal distance or duration to get ALL
            new Object[]{DURATION, null, DURATION, null, Completion.ALL},
            new Object[]{null, DISTANCE, null, DISTANCE, Completion.ALL},
            // only based on goal measurement, not extra total measurment
            new Object[]{DURATION, null, DURATION, DISTANCE_HALF, Completion.ALL},
            new Object[]{DURATION, null, DURATION_HALF, DISTANCE, Completion.SOME},
            new Object[]{null, DISTANCE, DURATION_HALF, DISTANCE, Completion.ALL},
            new Object[]{null, DISTANCE, DURATION, DISTANCE_HALF, Completion.SOME},
            // goal without actual means NONE
            new Object[]{DURATION, null, null, null, Completion.NONE},
            new Object[]{null, DISTANCE, null, null, Completion.NONE},
            new Object[]{DURATION, DISTANCE, null, null, Completion.NONE},
            // any actual means SOME
            new Object[]{DURATION, null, DURATION_HALF, null, Completion.SOME},
            new Object[]{DURATION, null, null, DISTANCE, Completion.SOME},
            new Object[]{null, DISTANCE, DURATION, null, Completion.SOME},
            new Object[]{null, DISTANCE, null, DISTANCE_HALF, Completion.SOME},
            // must meet both goals to get ALL
            new Object[]{DURATION, DISTANCE, DURATION_HALF, DISTANCE, Completion.SOME},
            new Object[]{DURATION, DISTANCE, DURATION, DISTANCE_HALF, Completion.SOME},
            new Object[]{DURATION, DISTANCE, DURATION, DISTANCE, Completion.ALL},
        };
    }
}
