package com.weizilla.workouts.interactor;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.verify;

public class DeletePlanTest extends PlanTest {
    private DeletePlan deletePlan;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        deletePlan = new DeletePlan(planStore);
    }

    @Test
    public void deletePlanShouldDeletePlanFromStore() throws Exception {
        deletePlan.delete(ID);
        verify(planStore).delete(ID);
    }
}