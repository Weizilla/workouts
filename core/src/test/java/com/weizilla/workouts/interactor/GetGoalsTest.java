package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.Goal;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class GetGoalsTest extends GoalTest {
    private GetGoals getGoals;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        getGoals = new GetGoals(goalStore);
    }

    @Test
    public void getShouldReturnPlanById() throws Exception {
        when(goalStore.get(ID)).thenReturn(goal);
        Goal actual = getGoals.get(ID);
        assertThat(actual).isEqualTo(goal);
    }

    @Test
    public void getShouldReturnNull() throws Exception {
        Goal actual = getGoals.get(ID);
        assertThat(actual).isNull();
    }

    @Test
    public void getShouldReturnPlanByDate() throws Exception {
        when(goalStore.get(DATE)).thenReturn(Collections.singletonList(goal));
        List<Goal> actual = getGoals.get(DATE);
        assertThat(actual).containsExactly(goal);
    }

    @Test
    public void getShouldReturnEmptyListIfNoPlansFound() throws Exception {
        List<Goal> actual = getGoals.get(DATE);
        assertThat(actual).isEmpty();
    }

    @Test
    public void getReturnAllPlans() throws Exception {
        when(goalStore.getAll()).thenReturn(Collections.singletonList(goal));
        List<Goal> actual = getGoals.getAll();
        assertThat(actual).containsExactly(goal);
    }
}