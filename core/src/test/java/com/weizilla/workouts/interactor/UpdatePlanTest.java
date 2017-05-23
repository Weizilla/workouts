package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.ImmutablePlan;
import com.weizilla.workouts.entity.Plan;
import com.weizilla.workouts.entity.TimeOfDay;
import org.junit.Before;
import org.junit.Test;
import tec.uom.se.quantity.Quantities;

import java.time.Duration;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static systems.uom.common.USCustomary.MILE;

public class UpdatePlanTest extends PlanTest {
    private UpdatePlan updatePlan;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        updatePlan = new UpdatePlan(planStore);
    }

    @Test
    public void updatesShouldUpdatePlanWithNewPlan() throws Exception {
        Plan newPlan = ImmutablePlan.copyOf(plan)
            .withType("NEW TYPE")
            .withDate(DATE.plusDays(1))
            .withTimeOfDay(TimeOfDay.AFTERNOON)
            .withDuration(Duration.ofMinutes(1))
            .withDistance(Quantities.getQuantity(2.0, MILE))
            .withNotes("NOTES");

        updatePlan.updatePlan(newPlan);
        verify(planStore).update(newPlan);
        verify(planStore, never()).update(plan);
    }

}