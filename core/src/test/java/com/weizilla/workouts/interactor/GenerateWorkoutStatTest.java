package com.weizilla.workouts.interactor;

import com.weizilla.distance.Distance;
import com.weizilla.workouts.entity.Activity;
import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.entity.ImmutableActivity;
import com.weizilla.workouts.entity.ImmutableRecord;
import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.entity.TestEntity;
import com.weizilla.workouts.entity.Workout;
import com.weizilla.workouts.entity.WorkoutAssert;
import com.weizilla.workouts.store.GarminStore;
import com.weizilla.workouts.store.MemoryGarminStore;
import com.weizilla.workouts.store.MemoryRecordStore;
import com.weizilla.workouts.store.RecordStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.weizilla.workouts.entity.TestEntity.DATE;
import static com.weizilla.workouts.entity.TestEntity.DISTANCE;
import static com.weizilla.workouts.entity.TestEntity.DURATION;
import static com.weizilla.workouts.entity.TestEntity.START;
import static com.weizilla.workouts.entity.TestEntity.TYPE;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class GenerateWorkoutStatTest {
    private RecordStore recordStore;
    private GarminStore garminStore;
    private GenerateWorkoutStat generateWorkoutStat;
    private Activity activity;
    private Record record;
    private Goal goal;

    @Before
    public void setUp() throws Exception {
        garminStore = new MemoryGarminStore();
        recordStore = new MemoryRecordStore();
        generateWorkoutStat = new GenerateWorkoutStat(recordStore, garminStore);
        activity = TestEntity.createActivity();
        record = TestEntity.createRecord();
        goal = TestEntity.createGoal();
    }

    @Test
    public void returnsEmptyListWithNothing() throws Exception {
        assertThat(generateWorkoutStat.get(DATE)).isEmpty();
    }

    @Test
    public void returnsMatchedWorkout() throws Exception {
        garminStore.add(activity);
        recordStore.add(record);

        List<Workout> workouts = generateWorkoutStat.get((DATE));
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasType((TYPE));
        WorkoutAssert.assertThat(workout).hasDate((DATE));
        WorkoutAssert.assertThat(workout).hasRecord(record);
        WorkoutAssert.assertThat(workout).hasActivities(activity);
        WorkoutAssert.assertThat(workout).hasTotalDistance((DISTANCE));
        WorkoutAssert.assertThat(workout).hasTotalDuration((DURATION));
    }

    @Test
    public void doesNotReturnWorkoutIfTypeMismatches() throws Exception {
        Record mismatchRecord = ImmutableRecord.copyOf(record)
            .withType("NEW TYPE");

        garminStore.add(activity);
        recordStore.add(mismatchRecord);

        assertThat(generateWorkoutStat.get((DATE))).isEmpty();
    }

    @Test
    public void usesRecordDurationIfExists() throws Exception {
        Duration recordDuration = (DURATION).plusHours(1);
        record = ImmutableRecord.copyOf(record).withDuration(recordDuration);

        garminStore.add(activity);
        recordStore.add(record);

        List<Workout> workouts = generateWorkoutStat.get((DATE));
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasTotalDuration(recordDuration);
        assertThat(workout.getTotalDuration()).isNotEqualTo((DURATION));
    }

    @Test
    public void usesActivityDurationIfNoneInRecord() throws Exception {
        record = ImmutableRecord.copyOf(record).withDuration(null);

        garminStore.add(activity);
        recordStore.add(record);

        List<Workout> workouts = generateWorkoutStat.get((DATE));
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasTotalDuration((DURATION));
    }

    @Test
    public void useRecordDistanceIfExists() throws Exception {
        Distance recordDistance = (DISTANCE).multipliedBy(2);
        record = ImmutableRecord.copyOf(record).withDistance(recordDistance);

        garminStore.add(activity);
        recordStore.add(record);

        List<Workout> workouts = generateWorkoutStat.get((DATE));
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasTotalDistance(recordDistance);
        assertThat(workout.getTotalDistance()).isNotEqualTo((DISTANCE));
    }

    @Test
    public void useActivityDistanceIfNoneInRecord() throws Exception {
        record = ImmutableRecord.copyOf(record).withDistance(null);

        garminStore.add(activity);
        recordStore.add(record);

        List<Workout> workouts = generateWorkoutStat.get((DATE));
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasTotalDistance((DISTANCE));
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

        garminStore.addAll(activities);
        recordStore.add(record);

        List<Workout> workouts = generateWorkoutStat.get((DATE));
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        assertThat(workout.getActivities()).isEqualTo(activities);
    }

    @Test
    public void combinesActivityDurationsIfMultipleMatches() throws Exception {
        record = ImmutableRecord.copyOf(record).withDuration(null);

        List<Activity> activities = new ArrayList<>();
        int totalDuration = 0;
        for (long i = 1; i < 10; i++) {
            Activity multiple = ImmutableActivity.copyOf(activity)
                .withId(i)
                .withDuration(Duration.ofHours(i));
            activities.add(multiple);
            totalDuration += i;
        }
        Duration expected = Duration.ofHours(totalDuration);

        garminStore.addAll(activities);
        recordStore.add(record);

        List<Workout> workouts = generateWorkoutStat.get((DATE));
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasTotalDuration(expected);
    }

    @Test
    public void combinesActivityDistancesIfMultipleMatches() throws Exception {
        record = ImmutableRecord.copyOf(record).withDistance(null);

        List<Activity> activities = new ArrayList<>();
        double totalDistance = 0;
        for (long i = 1; i < 10; i++) {
            Distance dist = Distance.ofKilometers(i);
            Activity multiple = ImmutableActivity.copyOf(activity).withId(i).withDistance(dist);
            activities.add(multiple);
            totalDistance += i;
        }
        Distance expected = Distance.ofKilometers(totalDistance);

        garminStore.addAll(activities);
        recordStore.add(record);

        List<Workout> workouts = generateWorkoutStat.get((DATE));
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasTotalDistance(expected);
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
        garminStore.add(activity);
        recordStore.add(record);

        List<Workout> workouts = generateWorkoutStat.getAll();
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasType((TYPE));
        WorkoutAssert.assertThat(workout).hasDate((DATE));
        WorkoutAssert.assertThat(workout).hasActivities(activity);
        WorkoutAssert.assertThat(workout).hasRecord(record);
        WorkoutAssert.assertThat(workout).hasTotalDuration((DURATION));
        WorkoutAssert.assertThat(workout).hasTotalDistance((DISTANCE));
    }

    @Test
    public void getsAllWorkoutsByMultipleDates() throws Exception {
        garminStore.add(activity);
        recordStore.add(record);

        UUID newRecordId = UUID.randomUUID();
        LocalDateTime newStart = START.plusDays(10);
        LocalDate newDate = newStart.toLocalDate();
        Record newRecord = ImmutableRecord.copyOf(record)
            .withId(newRecordId)
            .withDate(newDate);
        recordStore.add(newRecord);

        long newGarminId = activity.getId() + 100;
        Activity newActivity = ImmutableActivity.copyOf(activity)
            .withId(newGarminId)
            .withStartTime(newStart);
        garminStore.add(newActivity);

        List<Workout> workouts = generateWorkoutStat.getAll();
        assertThat(workouts).hasSize(2);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasType((TYPE));
        WorkoutAssert.assertThat(workout).hasDate((DATE));
        WorkoutAssert.assertThat(workout).hasActivities(activity);
        WorkoutAssert.assertThat(workout).hasRecord(record);
        WorkoutAssert.assertThat(workout).hasTotalDuration((DURATION));
        WorkoutAssert.assertThat(workout).hasTotalDistance((DISTANCE));

        workout = workouts.get(1);
        WorkoutAssert.assertThat(workout).hasType((TYPE));
        WorkoutAssert.assertThat(workout).hasDate(newDate);
        WorkoutAssert.assertThat(workout).hasRecord(newRecord);
        WorkoutAssert.assertThat(workout).hasActivities(newActivity);
        WorkoutAssert.assertThat(workout).hasTotalDuration((DURATION));
        WorkoutAssert.assertThat(workout).hasTotalDistance((DISTANCE));
    }
}
