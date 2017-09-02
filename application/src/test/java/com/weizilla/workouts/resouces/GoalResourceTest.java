package com.weizilla.workouts.resouces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.weizilla.distance.Distance;
import com.weizilla.workouts.entity.ImmutableGoal;
import com.weizilla.workouts.entity.ObjectMappers;
import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.entity.TimeOfDay;
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
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GoalResourceTest {
    protected static final UUID ID = UUID.randomUUID();
    protected static final String TYPE = "TYPE";
    protected static final TimeOfDay TIME_OF_DAY = TimeOfDay.MORNING;
    protected static final LocalDate DATE = LocalDate.now();
    protected static final Duration DURATION = Duration.ofHours(1);
    protected static final Distance DISTANCE = Distance.ofMiles(1);
    protected static final String NOTES = "NOTES";
    private static final Goal goal = ImmutableGoal.builder()
        .id(ID)
        .type(TYPE)
        .date(DATE)
        .duration(DURATION)
        .timeOfDay(TIME_OF_DAY)
        .distance(DISTANCE)
        .notes(NOTES)
        .build();

    private static final CreateGoal createGoal = mock(CreateGoal.class);
    private static final GetGoals getGoals = mock(GetGoals.class);
    private static final UpdateGoal updateGoal = mock(UpdateGoal.class);
    private static final DeleteGoal deleteGoal = mock(DeleteGoal.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
        .addResource(new GoalResource(createGoal, getGoals, deleteGoal, updateGoal))
        .setMapper(ObjectMappers.OBJECT_MAPPER)
        .build();
    private static final TypeReference<List<Goal>> TYPE_REF = new TypeReference<List<Goal>>() {
    };

    @After
    public void tearDown() throws Exception {
        reset(createGoal, getGoals, updateGoal, deleteGoal);
    }

    @Test
    public void addsGoal() throws Exception {
        Goal create = ImmutableGoal.builder()
            .type(TYPE)
            .date(DATE)
            .timeOfDay(TIME_OF_DAY)
            .duration(DURATION)
            .distance(DISTANCE)
            .notes(NOTES)
            .build();

        resources.target("/goals").request()
            .post(Entity.entity(create, MediaType.APPLICATION_JSON_TYPE));

        verify(createGoal).create(create);
    }

    @Test
    public void getsAllGoals() throws Exception {
        when(getGoals.getAll()).thenReturn(singletonList(goal));
        String jsonResult = resources.target("/goals").request().get(String.class);
        List<Goal> results = ObjectMappers.OBJECT_MAPPER.readValue(jsonResult, TYPE_REF);
        assertThat(results).containsExactly(goal);
    }

    @Test
    public void getsGoalByDate() throws Exception {
        when(getGoals.get(DATE)).thenReturn(singletonList(goal));

        String jsonResult = resources.target("/goals")
            .queryParam("date", DATE).request().get(String.class);
        List<Goal> results = ObjectMappers.OBJECT_MAPPER.readValue(jsonResult, TYPE_REF);
        assertThat(results).containsExactly(goal);
    }

    @Test
    public void getsGoalById() throws Exception {
        when(getGoals.get(ID)).thenReturn(goal);

        String jsonResult = resources.target("/goals/" + ID).request().get(String.class);
        Goal result = ObjectMappers.OBJECT_MAPPER.readValue(jsonResult, Goal.class);
        assertThat(result).isEqualTo(goal);
    }

    @Test
    public void updatesGoal() throws Exception {
        resources.target("/goals").request()
            .put(Entity.entity(goal, MediaType.APPLICATION_JSON_TYPE));

        verify(updateGoal).updateGoal(goal);
    }

    @Test
    public void deletesGoal() throws Exception {
        resources.target("/goals/" + ID).request().delete();

        verify(deleteGoal).delete(ID);
    }
}