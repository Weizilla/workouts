package com.weizilla.workouts.interactor;

import com.weizilla.garmin.entity.Activity;
import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.entity.Workout;
import com.weizilla.workouts.entity.WorkoutAssert;
import com.weizilla.workouts.store.GarminStore;
import com.weizilla.workouts.store.RecordStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import tec.uom.se.quantity.Quantities;

import javax.measure.Quantity;
import javax.measure.quantity.Length;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static systems.uom.common.USCustomary.MILE;

@RunWith(MockitoJUnitRunner.class)
public class GetWorkoutsTest {
    @Mock
    private RecordStore recordStore;
    @Mock
    private GarminStore garminStore;
    private GetWorkouts getWorkouts;
    private LocalDateTime start;
    private long garminId;
    private String type;
    private UUID recordId;
    private String comment;
    private LocalDate date;
    private int rating;
    private Quantity<Length> distance;
    private Duration duration;
    private Activity activity;
    private Record record;

    @Before
    public void setUp() throws Exception {
        getWorkouts = new GetWorkouts(recordStore, garminStore);

        garminId = 1;
        type = "TYPE";
        start = LocalDateTime.now();
        duration = Duration.ofHours(1);
        distance = Quantities.getQuantity(10, MILE);
        activity = new Activity(garminId, type, duration, start, distance);

        recordId = UUID.randomUUID();
        comment = "COMMENT";
        date = start.toLocalDate();
        rating = 3;
        record = new Record(recordId, type, date, rating);
        record.setComment(comment);
        record.setDuration(duration);
    }

    @Test
    public void returnsEmptyListWithNothing() throws Exception {
        assertThat(getWorkouts.getWorkouts(date)).isEmpty();
    }

    @Test
    public void returnsMatchedWorkout() throws Exception {
        when(garminStore.get(date)).thenReturn(singletonList(activity));
        when(recordStore.get(date)).thenReturn(singletonList(record));

        List<Workout> workouts = getWorkouts.getWorkouts(date);
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasRecordId(recordId);
        WorkoutAssert.assertThat(workout).hasType(type);
        WorkoutAssert.assertThat(workout).hasStartTime(start);
        WorkoutAssert.assertThat(workout).hasRating(rating);
        WorkoutAssert.assertThat(workout).hasDuration(duration);
        WorkoutAssert.assertThat(workout).hasDistance(distance);
        WorkoutAssert.assertThat(workout).hasOnlyGarminIds(garminId);
        WorkoutAssert.assertThat(workout).hasComment(comment);
    }

    @Test
    public void doesNotReturnWorkoutIfTypeMismatches() throws Exception {
        Record mismatchRecord = new Record(recordId, "NEW TYPE", date, rating);

        when(garminStore.get(date)).thenReturn(singletonList(activity));
        when(recordStore.get(date)).thenReturn(singletonList(mismatchRecord));

        assertThat(getWorkouts.getWorkouts(date)).isEmpty();
    }

    @Test
    public void usesRecordDurationIfExists() throws Exception {
        Duration recordDuration = duration.plusHours(1);
        record.setDuration(recordDuration);

        when(garminStore.get(date)).thenReturn(singletonList(activity));
        when(recordStore.get(date)).thenReturn(singletonList(record));

        List<Workout> workouts = getWorkouts.getWorkouts(date);
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasDuration(recordDuration);
        assertThat(workout.getDuration()).isNotEqualTo(duration);
    }

    @Test
    public void usesActivityDurationIfNoneInRecord() throws Exception {
        record.setDuration(null);

        when(garminStore.get(date)).thenReturn(singletonList(activity));
        when(recordStore.get(date)).thenReturn(singletonList(record));

        List<Workout> workouts = getWorkouts.getWorkouts(date);
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasDuration(duration);
    }

    @Test
    public void useRecordDistanceIfExists() throws Exception {
        Quantity<Length> recordDistance = distance.multiply(2);
        record.setDistance(recordDistance);

        when(garminStore.get(date)).thenReturn(singletonList(activity));
        when(recordStore.get(date)).thenReturn(singletonList(record));

        List<Workout> workouts = getWorkouts.getWorkouts(date);
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasDistance(recordDistance);
        assertThat(workout.getDistance()).isNotEqualTo(distance);
    }

    @Test
    public void useActivityDistanceIfNoneInRecord() throws Exception {
        record.setDistance(null);

        when(garminStore.get(date)).thenReturn(singletonList(activity));
        when(recordStore.get(date)).thenReturn(singletonList(record));

        List<Workout> workouts = getWorkouts.getWorkouts(date);
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasDistance(distance);
    }

    @Test
    public void matchesMultipleActivitiesForSameType() throws Exception {
        List<Activity> activities = new ArrayList<>();
        List<Long> expectedIds = new ArrayList<>();
        for (long i = 0; i < 10; i++) {
            Activity activity = new Activity(i, type, duration, start, distance);
            expectedIds.add(i);
            activities.add(activity);
        }

        when(garminStore.get(date)).thenReturn(activities);
        when(recordStore.get(date)).thenReturn(singletonList(record));

        List<Workout> workouts = getWorkouts.getWorkouts(date);
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasOnlyGarminIds(expectedIds.toArray(new Long[]{}));
    }

    @Test
    public void combinesActivityDurationsIfMultipleMatches() throws Exception {
        List<Activity> activities = new ArrayList<>();
        int totalDuration = 0;
        for (int i = 1; i < 10; i++) {
            activities.add(new Activity(i, type, Duration.ofHours(i), start, distance));
            totalDuration += i;
        }
        Duration expected = Duration.ofHours(totalDuration);

        record.setDuration(null);
        when(garminStore.get(date)).thenReturn(activities);
        when(recordStore.get(date)).thenReturn(singletonList(record));

        List<Workout> workouts = getWorkouts.getWorkouts(date);
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasDuration(expected);
    }

    @Test
    public void combinesActivityDistancesIfMultipleMatches() throws Exception {
        List<Activity> activities = new ArrayList<>();
        double totalDistance = 0;
        for (int i = 1; i < 10; i++) {
            activities.add(new Activity(i, type, duration, start, Quantities.getQuantity((double) i, MILE)));
            totalDistance += i;
        }
        Quantity<Length> expected = Quantities.getQuantity(totalDistance, MILE);

        record.setDistance(null);
        when(garminStore.get(date)).thenReturn(activities);
        when(recordStore.get(date)).thenReturn(singletonList(record));

        List<Workout> workouts = getWorkouts.getWorkouts(date);
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasDistance(expected);
    }

    @Test
    public void useEarliestStartDateFromActivities() throws Exception {

    }

    @Test
    public void matchesMultipleTypesAtSameTime() throws Exception {

    }

    @Test
    public void usesActivityTypeMappingFile() throws Exception {

    }
}