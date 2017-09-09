package com.weizilla.workouts.interactor;

import com.weizilla.workouts.store.GoalStore;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static com.weizilla.workouts.entity.TestEntity.GOAL_ID;
import static org.mockito.Mockito.verify;

public class DeleteGoalTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    private GoalStore goalStore;
    private DeleteGoal deleteGoal;

    @Before
    public void setUp() throws Exception {
        deleteGoal = new DeleteGoal(goalStore);
    }

    @Test
    public void deletePlanShouldDeletePlanFromStore() throws Exception {
        deleteGoal.delete(GOAL_ID);
        verify(goalStore).delete(GOAL_ID);
    }
}