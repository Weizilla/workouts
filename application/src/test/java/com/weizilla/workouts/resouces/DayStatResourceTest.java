package com.weizilla.workouts.resouces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.weizilla.workouts.entity.DayStat;
import com.weizilla.workouts.entity.ObjectMappers;
import com.weizilla.workouts.entity.TestEntity;
import com.weizilla.workouts.interactor.WorkoutStatGenerator;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static com.weizilla.workouts.entity.TestEntity.DATE;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DayStatResourceTest {
    private static final DayStat DAY_STAT = TestEntity.createDayStat();
    private static final WorkoutStatGenerator GENERATE_WORKOUT_STAT = mock(WorkoutStatGenerator.class);
    private static final TypeReference<List<DayStat>> TYPE_REF = new TypeReference<List<DayStat>>() { };

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
        .addResource(new DayStatResource(GENERATE_WORKOUT_STAT))
        .setMapper(ObjectMappers.OBJECT_MAPPER)
        .build();

    @After
    public void tearDown() throws Exception {
        reset(GENERATE_WORKOUT_STAT);
    }

    @Test
    public void getsAllWorkouts() throws Exception {
        when(GENERATE_WORKOUT_STAT.generateAll()).thenReturn(singletonList(DAY_STAT));
        String jsonResult = RESOURCES.target("/stats").request().get(String.class);
        List<DayStat> results = ObjectMappers.OBJECT_MAPPER.readValue(jsonResult, TYPE_REF);
        assertThat(results).containsExactly(DAY_STAT);
    }

    @Test
    public void getsWorkoutByDate() throws Exception {
        when(GENERATE_WORKOUT_STAT.generate(DATE)).thenReturn(Optional.of(DAY_STAT));

        String jsonResult = RESOURCES.target("/stats").path(DATE.toString()).request().get(String.class);
        DayStat results = ObjectMappers.OBJECT_MAPPER.readValue(jsonResult, DayStat.class);
        assertThat(results).isEqualTo(DAY_STAT);
    }
}
