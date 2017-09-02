package com.weizilla.workouts.interactor;

import com.weizilla.distance.Distance;
import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.entity.ImmutableGoal;
import com.weizilla.workouts.entity.TimeOfDay;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class UpdateGoalTest extends GoalTest {
    private UpdateGoal updateGoal;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        updateGoal = new UpdateGoal(goalStore);
    }

    @Test
    public void updatesShouldUpdatePlanWithNewPlan() throws Exception {
        Goal newGoal = ImmutableGoal.copyOf(goal)
            .withType("NEW TYPE")
            .withDate(DATE.plusDays(1))
            .withTimeOfDay(TimeOfDay.AFTERNOON)
            .withDuration(Duration.ofMinutes(1))
            .withDistance(Distance.ofMiles(2))
            .withNotes("NOTES");

        updateGoal.updatePlan(newGoal);
        verify(goalStore).update(newGoal);
        verify(goalStore, never()).update(goal);
    }

}