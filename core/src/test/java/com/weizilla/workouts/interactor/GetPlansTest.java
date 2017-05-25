package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.Plan;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class GetPlansTest extends PlanTest {
    private GetPlans getPlans;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        getPlans = new GetPlans(planStore);
    }

    @Test
    public void getShouldReturnPlanById() throws Exception {
        when(planStore.get(ID)).thenReturn(plan);
        Plan actual = getPlans.get(ID);
        assertThat(actual).isEqualTo(plan);
    }

    @Test
    public void getShouldReturnNull() throws Exception {
        Plan actual = getPlans.get(ID);
        assertThat(actual).isNull();
    }

    @Test
    public void getShouldReturnPlanByDate() throws Exception {
        when(planStore.get(DATE)).thenReturn(Collections.singletonList(plan));
        List<Plan> actual = getPlans.get(DATE);
        assertThat(actual).containsExactly(plan);
    }

    @Test
    public void getShouldReturnEmptyListIfNoPlansFound() throws Exception {
        List<Plan> actual = getPlans.get(DATE);
        assertThat(actual).isEmpty();
    }

    @Test
    public void getReturnAllPlans() throws Exception {
        when(planStore.getAll()).thenReturn(Collections.singletonList(plan));
        List<Plan> actual = getPlans.getAll();
        assertThat(actual).containsExactly(plan);
    }
}