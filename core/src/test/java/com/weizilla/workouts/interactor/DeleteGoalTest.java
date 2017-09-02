package com.weizilla.workouts.interactor;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.verify;

public class DeleteGoalTest extends GoalTest {
    private DeleteGoal deleteGoal;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        deleteGoal = new DeleteGoal(goalStore);
    }

    @Test
    public void deletePlanShouldDeletePlanFromStore() throws Exception {
        deleteGoal.delete(ID);
        verify(goalStore).delete(ID);
    }
}