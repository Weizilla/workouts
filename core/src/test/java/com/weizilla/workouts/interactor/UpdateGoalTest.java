package com.weizilla.workouts.interactor;

import com.weizilla.distance.Distance;
import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.entity.ImmutableGoal;
import com.weizilla.workouts.entity.TestEntity;
import com.weizilla.workouts.entity.TimeOfDay;
import com.weizilla.workouts.store.GoalStore;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.time.Duration;

import static com.weizilla.workouts.entity.TestEntity.DATE;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class UpdateGoalTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    private GoalStore goalStore;
    private UpdateGoal updateGoal;
    private Goal goal;

    @Before
    public void setUp() throws Exception {
        goal = TestEntity.createGoal();
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

        updateGoal.updateGoal(newGoal);
        verify(goalStore).update(newGoal);
        verify(goalStore, never()).update(goal);
    }

}