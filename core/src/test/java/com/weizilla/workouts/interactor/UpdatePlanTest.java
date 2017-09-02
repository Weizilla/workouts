package com.weizilla.workouts.interactor;

import com.weizilla.distance.Distance;
import com.weizilla.workouts.entity.ImmutablePlan;
import com.weizilla.workouts.entity.Plan;
import com.weizilla.workouts.entity.TimeOfDay;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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
            .withDistance(Distance.ofMiles(2))
            .withNotes("NOTES");

        updatePlan.updatePlan(newPlan);
        verify(planStore).update(newPlan);
        verify(planStore, never()).update(plan);
    }

}