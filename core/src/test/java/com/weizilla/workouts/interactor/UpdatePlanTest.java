package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.Plan;
import com.weizilla.workouts.entity.TimeOfDay;
import org.junit.Before;
import org.junit.Test;
import tec.uom.se.quantity.Quantities;

import java.time.Duration;

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
        Plan newPlan = new Plan(ID, "NEW TYPE", DATE.plusDays(1), TimeOfDay.AFTERNOON);
        newPlan.setNotes("NEW COMMENT");
        newPlan.setDuration(Duration.ofMinutes(1));
        newPlan.setDistance(Quantities.getQuantity(2.0, MILE));

        updatePlan.updatePlan(plan);
        verify(planStore).update(plan);
    }

}