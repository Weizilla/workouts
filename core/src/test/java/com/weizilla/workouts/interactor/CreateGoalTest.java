package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.entity.GoalAssert;
import com.weizilla.workouts.entity.ImmutableGoal;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

public class CreateGoalTest extends GoalTest {
    private CreateGoal createGoal;
    private Goal createDto;

    @Before
    public void setUp() throws Exception {
        createDto = ImmutableGoal.builder()
            .type(TYPE)
            .date(DATE)
            .notes(NOTES)
            .timeOfDay(TIME_OF_DAY)
            .distance(DISTANCE)
            .duration(DURATION)
            .build();
        createGoal = new CreateGoal(goalStore);
    }

    @Test
    public void createShouldReturnNewRecordWithId() throws Exception {
        Goal actual = createGoal.create(createDto);
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
        GoalAssert.assertThat(actual).hasType(TYPE);
        GoalAssert.assertThat(actual).hasDate(DATE);
        GoalAssert.assertThat(actual).hasNotes(NOTES);
        GoalAssert.assertThat(actual).hasTimeOfDay(TIME_OF_DAY);
        GoalAssert.assertThat(actual).hasDuration(DURATION);
        GoalAssert.assertThat(actual).hasDistance(DISTANCE);
    }

    @Test
    public void createShouldStoreNewRecordInStore() throws Exception {
        Goal actual = createGoal.create(createDto);
        verify(goalStore).add(actual);
    }
}