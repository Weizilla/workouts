package com.weizilla.workouts.interactor;

import com.weizilla.distance.Distance;
import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.entity.ImmutableGoal;
import com.weizilla.workouts.entity.TestEntity;
import com.weizilla.workouts.store.GoalStore;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

public class CreateGoalTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    private GoalStore goalStore;
    private CreateGoal createGoal;
    private Goal goal;

    @Before
    public void setUp() throws Exception {
        goal = TestEntity.createGoal();
        createGoal = new CreateGoal(goalStore);
    }

    @Test
    public void createShouldReturnNewGoalWithId() throws Exception {
        Goal actual = createGoal.create(goal);
        assertThat(actual).isEqualTo(goal);
    }

    @Test
    public void ignoresZeroDurationAndDistanceWhenAddingGoals() throws Exception {
        Goal withZeros = ImmutableGoal.copyOf(goal)
            .withDuration(Duration.ofSeconds(0))
            .withDistance(Distance.ofMeters(0));
        Goal withEmpties = ImmutableGoal.copyOf(goal)
            .withDistance(Optional.empty())
            .withDuration(Optional.empty());

        Goal actual = createGoal.create(withZeros);
        assertThat(actual).isEqualTo(withEmpties);
        verify(goalStore).add(withEmpties);
    }

    @Test
    public void createShouldStoreNewGoalInStore() throws Exception {
        Goal actual = createGoal.create(goal);
        verify(goalStore).add(actual);
    }
}
