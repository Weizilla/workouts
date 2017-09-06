package com.weizilla.workouts.resouces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.weizilla.distance.Distance;
import com.weizilla.workouts.entity.ImmutableWorkout;
import com.weizilla.workouts.entity.ObjectMappers;
import com.weizilla.workouts.entity.Workout;
import com.weizilla.workouts.interactor.GenerateWorkoutStat;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WorkoutResourceTest {
    protected static final UUID ID = UUID.randomUUID();
    protected static final String TYPE = "TYPE";
    protected static final boolean OUTDOOR = true;
    protected static final LocalDate DATE = LocalDate.now();
    protected static final LocalDateTime START = LocalDateTime.now();
    protected static final int RATING = 3;
    protected static final Duration DURATION = Duration.ofHours(1);
    protected static final Distance DISTANCE = Distance.ofMiles(1);
    protected static final String COMMENT = "COMMENT";
    protected static final long GARMIN_ID = 1000;
    private static final Workout workout = ImmutableWorkout.builder()
        .type(TYPE)
        .date(DATE)
        .totalDuration(DURATION)
        .totalDistance(DISTANCE)
        .build();

    private static final GenerateWorkoutStat GENERATE_WORKOUT_STAT = mock(GenerateWorkoutStat.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
        .addResource(new WorkoutResource(GENERATE_WORKOUT_STAT))
        .setMapper(ObjectMappers.OBJECT_MAPPER)
        .build();
    private static final TypeReference<List<Workout>> TYPE_REF = new TypeReference<List<Workout>>() {
    };

    @After
    public void tearDown() throws Exception {
        reset(GENERATE_WORKOUT_STAT);
    }

    @Test
    public void getsAllRecords() throws Exception {
        when(GENERATE_WORKOUT_STAT.getAll()).thenReturn(singletonList(workout));
        String jsonResult = resources.target("/workouts").request().get(String.class);
        List<Workout> results = ObjectMappers.OBJECT_MAPPER.readValue(jsonResult, TYPE_REF);
        assertThat(results).containsExactly(workout);
    }

    @Test
    public void getsRecordByDate() throws Exception {
        when(GENERATE_WORKOUT_STAT.get(DATE)).thenReturn(singletonList(workout));

        String jsonResult = resources.target("/workouts")
            .queryParam("date", DATE).request().get(String.class);
        List<Workout> results = ObjectMappers.OBJECT_MAPPER.readValue(jsonResult, TYPE_REF);
        assertThat(results).containsExactly(workout);
    }
}