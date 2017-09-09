package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.entity.TestEntity;
import com.weizilla.workouts.store.GoalStore;
import com.weizilla.workouts.store.MemoryGoalStore;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.weizilla.workouts.entity.TestEntity.DATE;
import static com.weizilla.workouts.entity.TestEntity.GOAL_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class GetGoalsTest {
    private GoalStore goalStore;
    private GetGoals getGoals;
    private Goal goal;

    @Before
    public void setUp() throws Exception {
        goalStore = new MemoryGoalStore();
        getGoals = new GetGoals(goalStore);
        goal = TestEntity.createGoal();
    }

    @Test
    public void getShouldReturnPlanById() throws Exception {
        goalStore.add(goal);
        Goal actual = getGoals.get(GOAL_ID);
        assertThat(actual).isEqualTo(goal);
    }

    @Test
    public void getShouldReturnNull() throws Exception {
        Goal actual = getGoals.get(GOAL_ID);
        assertThat(actual).isNull();
    }

    @Test
    public void getShouldReturnPlanByDate() throws Exception {
        goalStore.add(goal);
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
        goalStore.add(goal);
        List<Goal> actual = getGoals.getAll();
        assertThat(actual).containsExactly(goal);
    }
}