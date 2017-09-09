package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.entity.TestEntity;
import com.weizilla.workouts.store.GoalStore;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

public class CreateGoalTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    private GoalStore goalStore;
    private CreateGoal createGoal;
    private Goal createDto;

    @Before
    public void setUp() throws Exception {
        createDto = TestEntity.createGoal();
        createGoal = new CreateGoal(goalStore);
    }

    @Test
    public void createShouldReturnNewRecordWithId() throws Exception {
        Goal actual = createGoal.create(createDto);
        assertThat(actual).isEqualTo(createDto);
    }

    @Test
    public void createShouldStoreNewRecordInStore() throws Exception {
        Goal actual = createGoal.create(createDto);
        verify(goalStore).add(actual);
    }
}