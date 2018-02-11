package com.weizilla.workouts.interactor;

import com.weizilla.distance.Distance;
import com.weizilla.workouts.entity.Completion;
import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.entity.TestEntity;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class CompletionCalculatorTest {
    private CompletionCalculator calculator;

    @Before
    public void setUp() throws Exception {
        calculator = new CompletionCalculator();
    }

    @Test
    public void completionIsGoalIfGoalIsInFuture() throws Exception {
        LocalDate future = LocalDate.now().plusDays(1);
        Goal goal = TestEntity.createGoal(future);
        Completion completion = calculator.calculate(goal, null, null);
        assertThat(completion).isEqualTo(Completion.GOAL);
    }


    @Test
    public void completionIsAllIfNoGoalWithDuration() throws Exception {
        Completion completion = calculator.calculate(null, Duration.ofHours(1), null);
        assertThat(completion).isEqualTo(Completion.ALL);
    }

    @Test
    public void completionIsAllIfNoGoalWithDistance() throws Exception {
        Completion completion = calculator.calculate(null, null, Distance.ofMiles(1));
        assertThat(completion).isEqualTo(Completion.ALL);
    }

    @Test
    public void completionIsAllIfNoGoalDistanceOrDuration() throws Exception {
        Completion completion = calculator.calculate(null, null, null);
        assertThat(completion).isEqualTo(Completion.ALL);
    }
}
