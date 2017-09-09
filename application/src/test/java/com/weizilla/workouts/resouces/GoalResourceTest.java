package com.weizilla.workouts.resouces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.entity.ObjectMappers;
import com.weizilla.workouts.entity.TestEntity;
import com.weizilla.workouts.interactor.CreateGoal;
import com.weizilla.workouts.interactor.DeleteGoal;
import com.weizilla.workouts.interactor.GetGoals;
import com.weizilla.workouts.interactor.UpdateGoal;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static com.weizilla.workouts.entity.TestEntity.DATE;
import static com.weizilla.workouts.entity.TestEntity.GOAL_ID;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GoalResourceTest {
    private static final Goal GOAL = TestEntity.createGoal();
    private static final CreateGoal CREATE_GOAL = mock(CreateGoal.class);
    private static final GetGoals GET_GOALS = mock(GetGoals.class);
    private static final UpdateGoal UPDATE_GOAL = mock(UpdateGoal.class);
    private static final DeleteGoal DELETE_GOAL = mock(DeleteGoal.class);
    private static final TypeReference<List<Goal>> TYPE_REF = new TypeReference<List<Goal>>() { };

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
        .addResource(new GoalResource(CREATE_GOAL, GET_GOALS, DELETE_GOAL, UPDATE_GOAL))
        .setMapper(ObjectMappers.OBJECT_MAPPER)
        .build();

    @After
    public void tearDown() throws Exception {
        reset(CREATE_GOAL, GET_GOALS, UPDATE_GOAL, DELETE_GOAL);
    }

    @Test
    public void addsGoal() throws Exception {
        RESOURCES.target("/goals").request()
            .post(Entity.entity(GOAL, MediaType.APPLICATION_JSON_TYPE));

        verify(CREATE_GOAL).create(GOAL);
    }

    @Test
    public void getsAllGoals() throws Exception {
        when(GET_GOALS.getAll()).thenReturn(singletonList(GOAL));
        String jsonResult = RESOURCES.target("/goals").request().get(String.class);
        List<Goal> results = ObjectMappers.OBJECT_MAPPER.readValue(jsonResult, TYPE_REF);
        assertThat(results).containsExactly(GOAL);
    }

    @Test
    public void getsGoalByDate() throws Exception {
        when(GET_GOALS.get(DATE)).thenReturn(singletonList(GOAL));

        String jsonResult = RESOURCES.target("/goals")
            .queryParam("date", DATE).request().get(String.class);
        List<Goal> results = ObjectMappers.OBJECT_MAPPER.readValue(jsonResult, TYPE_REF);
        assertThat(results).containsExactly(GOAL);
    }

    @Test
    public void getsGoalById() throws Exception {
        when(GET_GOALS.get(GOAL_ID)).thenReturn(GOAL);

        String jsonResult = RESOURCES.target("/goals/" + GOAL_ID).request().get(String.class);
        Goal result = ObjectMappers.OBJECT_MAPPER.readValue(jsonResult, Goal.class);
        assertThat(result).isEqualTo(GOAL);
    }

    @Test
    public void updatesGoal() throws Exception {
        RESOURCES.target("/goals").request()
            .put(Entity.entity(GOAL, MediaType.APPLICATION_JSON_TYPE));

        verify(UPDATE_GOAL).updateGoal(GOAL);
    }

    @Test
    public void deletesGoal() throws Exception {
        RESOURCES.target("/goals/" + GOAL_ID).request().delete();

        verify(DELETE_GOAL).delete(GOAL_ID);
    }
}