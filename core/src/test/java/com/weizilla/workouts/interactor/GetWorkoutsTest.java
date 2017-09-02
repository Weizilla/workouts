package com.weizilla.workouts.interactor;

import com.weizilla.distance.Distance;
import com.weizilla.garmin.entity.Activity;
import com.weizilla.garmin.entity.ImmutableActivity;
import com.weizilla.workouts.entity.ImmutableRecord;
import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.entity.Workout;
import com.weizilla.workouts.entity.WorkoutAssert;
import com.weizilla.workouts.store.GarminStore;
import com.weizilla.workouts.store.RecordStore;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
    private boolean outdoor;
    private UUID recordId;
    private String comment;
    private LocalDate date;
    private int rating;
    private Distance distance;
    private Duration duration;
    private Activity activity;
    private Record record;

    @Before
    public void setUp() throws Exception {
        getWorkouts = new GetWorkouts(recordStore, garminStore);

        garminId = 1;
        type = "TYPE";
        outdoor = true;
        start = LocalDateTime.now();
        duration = Duration.ofHours(1);
        distance = Distance.ofMiles(2);
        activity = ImmutableActivity.builder()
            .id(garminId)
            .type(type)
            .start(start)
            .duration(duration)
            .distance(distance)
            .build();

        recordId = UUID.randomUUID();
        comment = "COMMENT";
        date = start.toLocalDate();
        rating = 3;
        record = ImmutableRecord.builder()
            .id(recordId)
            .type(type)
            .outdoor(outdoor)
            .date(date)
            .rating(rating)
            .distance(distance)
            .duration(duration)
            .comment(comment)
            .build();
    }

    @Test
    public void returnsEmptyListWithNothing() throws Exception {
        assertThat(getWorkouts.get(date)).isEmpty();
    }

    @Test
    public void returnsMatchedWorkout() throws Exception {
        when(garminStore.get(date)).thenReturn(singletonList(activity));
        when(recordStore.get(date)).thenReturn(singletonList(record));

        List<Workout> workouts = getWorkouts.get(date);
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
        Record mismatchRecord = ImmutableRecord.copyOf(record)
            .withType("NEW TYPE");

        when(garminStore.get(date)).thenReturn(singletonList(activity));
        when(recordStore.get(date)).thenReturn(singletonList(mismatchRecord));

        assertThat(getWorkouts.get(date)).isEmpty();
    }

    @Test
    public void usesRecordDurationIfExists() throws Exception {
        Duration recordDuration = duration.plusHours(1);
        record = ImmutableRecord.copyOf(record).withDuration(recordDuration);

        when(garminStore.get(date)).thenReturn(singletonList(activity));
        when(recordStore.get(date)).thenReturn(singletonList(record));

        List<Workout> workouts = getWorkouts.get(date);
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasDuration(recordDuration);
        assertThat(workout.getDuration()).isNotEqualTo(duration);
    }

    @Test
    public void usesActivityDurationIfNoneInRecord() throws Exception {
        record = ImmutableRecord.copyOf(record).withDuration(null);

        when(garminStore.get(date)).thenReturn(singletonList(activity));
        when(recordStore.get(date)).thenReturn(singletonList(record));

        List<Workout> workouts = getWorkouts.get(date);
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasDuration(duration);
    }

    @Test
    public void useRecordDistanceIfExists() throws Exception {
        Distance recordDistance = distance.multipliedBy(2);
        record = ImmutableRecord.copyOf(record).withDistance(recordDistance);

        when(garminStore.get(date)).thenReturn(singletonList(activity));
        when(recordStore.get(date)).thenReturn(singletonList(record));

        List<Workout> workouts = getWorkouts.get(date);
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasDistance(recordDistance);
        assertThat(workout.getDistance()).isNotEqualTo(distance);
    }

    @Test
    public void useActivityDistanceIfNoneInRecord() throws Exception {
        record = ImmutableRecord.copyOf(record).withDistance(null);

        when(garminStore.get(date)).thenReturn(singletonList(activity));
        when(recordStore.get(date)).thenReturn(singletonList(record));

        List<Workout> workouts = getWorkouts.get(date);
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasDistance(distance);
    }

    @Test
    public void matchesMultipleActivitiesForSameType() throws Exception {
        List<Activity> activities = new ArrayList<>();
        List<Long> expectedIds = new ArrayList<>();
        for (long i = 0; i < 10; i++) {
            Activity multiple = ImmutableActivity.copyOf(activity).withId(i);
            expectedIds.add(i);
            activities.add(multiple);
        }

        when(garminStore.get(date)).thenReturn(activities);
        when(recordStore.get(date)).thenReturn(singletonList(record));

        List<Workout> workouts = getWorkouts.get(date);
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasOnlyGarminIds(expectedIds.toArray(new Long[]{}));
    }

    @Test
    public void combinesActivityDurationsIfMultipleMatches() throws Exception {
        record = ImmutableRecord.copyOf(record).withDuration(null);

        List<Activity> activities = new ArrayList<>();
        int totalDuration = 0;
        for (int i = 1; i < 10; i++) {
            Activity multiple = ImmutableActivity.copyOf(activity)
                .withId(i)
                .withDuration(Duration.ofHours(i));
            activities.add(multiple);
            totalDuration += i;
        }
        Duration expected = Duration.ofHours(totalDuration);

        when(garminStore.get(date)).thenReturn(activities);
        when(recordStore.get(date)).thenReturn(singletonList(record));

        List<Workout> workouts = getWorkouts.get(date);
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasDuration(expected);
    }

    @Test
    public void combinesActivityDistancesIfMultipleMatches() throws Exception {
        record = ImmutableRecord.copyOf(record).withDistance(null);

        List<Activity> activities = new ArrayList<>();
        double totalDistance = 0;
        for (int i = 1; i < 10; i++) {
            Distance dist = Distance.ofKilometers(i);
            Activity multiple = ImmutableActivity.copyOf(activity).withId(i).withDistance(dist);
            activities.add(multiple);
            totalDistance += i;
        }
        Distance expected = Distance.ofKilometers(totalDistance);

        when(garminStore.get(date)).thenReturn(activities);
        when(recordStore.get(date)).thenReturn(singletonList(record));

        List<Workout> workouts = getWorkouts.get(date);
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

    @Test
    public void getsAllWorkoutsBySingleDate() throws Exception {
        when(garminStore.get(date)).thenReturn(singletonList(activity));
        when(recordStore.get(date)).thenReturn(singletonList(record));

        when(garminStore.getAll()).thenReturn(Lists.newArrayList(activity));
        when(recordStore.getAll()).thenReturn(Lists.newArrayList(record));

        List<Workout> workouts = getWorkouts.getAll();
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
    public void getsAllWorkoutsByMultipleDates() throws Exception {
        when(garminStore.get(date)).thenReturn(singletonList(activity));
        when(recordStore.get(date)).thenReturn(singletonList(record));

        UUID newRecordId = UUID.randomUUID();
        LocalDateTime newStart = start.plusDays(10);
        LocalDate newDate = newStart.toLocalDate();
        Record newRecord = ImmutableRecord.copyOf(record)
            .withId(newRecordId)
            .withDate(newDate);
        when(recordStore.get(newDate)).thenReturn(singletonList(newRecord));

        long newGarminId = garminId + 100;
        Activity newActivity = ImmutableActivity.copyOf(activity)
            .withId(newGarminId)
            .withStart(newStart);
        when(garminStore.get(newDate)).thenReturn(singletonList(newActivity));

        when(garminStore.getAll()).thenReturn(Lists.newArrayList(activity, newActivity));
        when(recordStore.getAll()).thenReturn(Lists.newArrayList(record, newRecord));

        List<Workout> workouts = getWorkouts.getAll();
        assertThat(workouts).hasSize(2);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasRecordId(recordId);
        WorkoutAssert.assertThat(workout).hasType(type);
        WorkoutAssert.assertThat(workout).hasStartTime(start);
        WorkoutAssert.assertThat(workout).hasRating(rating);
        WorkoutAssert.assertThat(workout).hasDuration(duration);
        WorkoutAssert.assertThat(workout).hasDistance(distance);
        WorkoutAssert.assertThat(workout).hasOnlyGarminIds(garminId);
        WorkoutAssert.assertThat(workout).hasComment(comment);

        workout = workouts.get(1);
        WorkoutAssert.assertThat(workout).hasRecordId(newRecordId);
        WorkoutAssert.assertThat(workout).hasType(type);
        WorkoutAssert.assertThat(workout).hasStartTime(newStart);
        WorkoutAssert.assertThat(workout).hasRating(rating);
        WorkoutAssert.assertThat(workout).hasDuration(duration);
        WorkoutAssert.assertThat(workout).hasDistance(distance);
        WorkoutAssert.assertThat(workout).hasOnlyGarminIds(newGarminId);
        WorkoutAssert.assertThat(workout).hasComment(comment);
    }
}