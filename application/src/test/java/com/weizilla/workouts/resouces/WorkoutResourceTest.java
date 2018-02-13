package com.weizilla.workouts.resouces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.weizilla.workouts.entity.ObjectMappers;
import com.weizilla.workouts.entity.TestEntity;
import com.weizilla.workouts.entity.WorkoutStat;
import com.weizilla.workouts.interactor.WorkoutStatGenerator;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

@RunWith(MockitoJUnitRunner.class)
public class WorkoutResourceTest {
    private static final WorkoutStat WORKOUT_STAT = TestEntity.createWorkout();
    private static final WorkoutStatGenerator GENERATE_WORKOUT_STAT = mock(WorkoutStatGenerator.class);
    private static final TypeReference<List<WorkoutStat>> TYPE_REF = new TypeReference<List<WorkoutStat>>() { };

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
        .addResource(new WorkoutResource(GENERATE_WORKOUT_STAT))
        .setMapper(ObjectMappers.OBJECT_MAPPER)
        .build();

    @After
    public void tearDown() throws Exception {
        reset(GENERATE_WORKOUT_STAT);
    }
//
//    @Test
//    public void getsAllWorkouts() throws Exception {
//        when(GENERATE_WORKOUT_STAT.getAll()).thenReturn(singletonList(WORKOUT_STAT));
//        String jsonResult = RESOURCES.target("/workouts").request().get(String.class);
//        List<WorkoutStat> results = ObjectMappers.OBJECT_MAPPER.readValue(jsonResult, TYPE_REF);
//        assertThat(results).containsExactly(WORKOUT_STAT);
//    }
//
//    @Test
//    public void getsWorkoutByDate() throws Exception {
//        when(GENERATE_WORKOUT_STAT.get(DATE)).thenReturn(singletonList(WORKOUT_STAT));
//
//        String jsonResult = RESOURCES.target("/workouts")
//            .queryParam("date", DATE).request().get(String.class);
//        List<WorkoutStat> results = ObjectMappers.OBJECT_MAPPER.readValue(jsonResult, TYPE_REF);
//        assertThat(results).containsExactly(WORKOUT_STAT);
//    }
}
