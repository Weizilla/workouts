package com.weizilla.workouts.interactor;

import com.weizilla.distance.Distance;
import com.weizilla.workouts.entity.Activity;
import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.entity.ImmutableActivity;
import com.weizilla.workouts.entity.ImmutableGoal;
import com.weizilla.workouts.entity.ImmutableRecord;
import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.entity.TestEntity;
import com.weizilla.workouts.entity.Workout;
import com.weizilla.workouts.entity.WorkoutAssert;
import com.weizilla.workouts.store.GarminStore;
import com.weizilla.workouts.store.GoalStore;
import com.weizilla.workouts.store.MemoryGarminStore;
import com.weizilla.workouts.store.MemoryGoalStore;
import com.weizilla.workouts.store.MemoryRecordStore;
import com.weizilla.workouts.store.RecordStore;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.weizilla.workouts.entity.TestEntity.DATE;
import static com.weizilla.workouts.entity.TestEntity.DISTANCE;
import static com.weizilla.workouts.entity.TestEntity.DURATION;
import static com.weizilla.workouts.entity.TestEntity.TYPE;
import static org.assertj.core.api.Assertions.assertThat;

public class GenerateWorkoutStatTest {
    private RecordStore recordStore;
    private GarminStore garminStore;
    private GoalStore goalStore;
    private GenerateWorkoutStat generateWorkoutStat;
    private Activity activity;
    private Record record;
    private Goal goal;

    @Before
    public void setUp() {
        garminStore = new MemoryGarminStore();
        recordStore = new MemoryRecordStore();
        goalStore = new MemoryGoalStore();
        generateWorkoutStat = new GenerateWorkoutStat(recordStore, garminStore, goalStore);
        activity = TestEntity.createActivity();
        record = TestEntity.createRecord();
        goal = TestEntity.createGoal();
    }

    @Test
    public void returnsEmptyListWithNothing() {
        assertThat(generateWorkoutStat.get(DATE)).isEmpty();
        assertThat(generateWorkoutStat.getAll()).isEmpty();
    }

    @Test
    public void returnsActivityForGivenDate() {
        garminStore.add(activity);

        List<Workout> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasType(TYPE);
        WorkoutAssert.assertThat(workout).hasDate(DATE);
        WorkoutAssert.assertThat(workout).hasOnlyActivities(activity);
    }

    @Test
    public void returnsAllActivityForGivenDate() {
        garminStore.add(activity);
        Activity activity2 = TestEntity.createActivity();
        garminStore.add(activity2);

        List<Workout> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasType(TYPE);
        WorkoutAssert.assertThat(workout).hasDate(DATE);
        WorkoutAssert.assertThat(workout).hasOnlyActivities(activity, activity2);
    }

    @Test
    public void returnsRecordsForGivenDate() {
        recordStore.add(record);

        List<Workout> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasType(TYPE);
        WorkoutAssert.assertThat(workout).hasDate(DATE);
        WorkoutAssert.assertThat(workout).hasRecord(record);
    }

    @Test
    public void returnsGoalForGivenDate() {
        goalStore.add(goal);

        List<Workout> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasType(TYPE);
        WorkoutAssert.assertThat(workout).hasDate(DATE);
        WorkoutAssert.assertThat(workout).hasGoal(goal);
    }

    @Test
    public void returnsMatchedWorkout() {
        garminStore.add(activity);
        recordStore.add(record);
        goalStore.add(goal);

        List<Workout> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasType(TYPE);
        WorkoutAssert.assertThat(workout).hasDate(DATE);
        WorkoutAssert.assertThat(workout).hasRecord(record);
        WorkoutAssert.assertThat(workout).hasOnlyActivities(activity);
        WorkoutAssert.assertThat(workout).hasGoal(goal);
    }

    @Test
    public void returnsMultipleWorkoutsForDifferentTypes() throws Exception {
        Record newType = ImmutableRecord.copyOf(record).withType("NEW TYPE");
        garminStore.add(activity);
        recordStore.add(newType);
        goalStore.add(goal);

        List<Workout> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(2);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasType("NEW TYPE");
        WorkoutAssert.assertThat(workout).hasDate(DATE);
        WorkoutAssert.assertThat(workout).hasRecord(newType);
        WorkoutAssert.assertThat(workout).hasNoActivities();
        WorkoutAssert.assertThat(workout).hasGoal(null);

        Workout workout2 = workouts.get(1);
        WorkoutAssert.assertThat(workout2).hasType(TYPE);
        WorkoutAssert.assertThat(workout2).hasDate(DATE);
        WorkoutAssert.assertThat(workout2).hasRecord(null);
        WorkoutAssert.assertThat(workout2).hasOnlyActivities(activity);
        WorkoutAssert.assertThat(workout2).hasGoal(goal);
    }

    @Test
    public void returnsMultipleWorkoutsForDifferentDates() throws Exception {
        LocalDate newDate = DATE.plusDays(1);
        Record newRecord = ImmutableRecord.copyOf(record).withDate(newDate);
        garminStore.add(activity);
        recordStore.add(newRecord);
        goalStore.add(goal);

        List<Workout> workouts = generateWorkoutStat.getAll();
        assertThat(workouts).hasSize(2);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasType(TYPE);
        WorkoutAssert.assertThat(workout).hasDate(DATE);
        WorkoutAssert.assertThat(workout).hasRecord(null);
        WorkoutAssert.assertThat(workout).hasOnlyActivities(activity);
        WorkoutAssert.assertThat(workout).hasGoal(goal);

        Workout workout2 = workouts.get(1);
        WorkoutAssert.assertThat(workout2).hasType(TYPE);
        WorkoutAssert.assertThat(workout2).hasDate(newDate);
        WorkoutAssert.assertThat(workout2).hasRecord(newRecord);
        WorkoutAssert.assertThat(workout2).hasNoActivities();
        WorkoutAssert.assertThat(workout2).hasGoal(null);
    }

    @Test
    public void usesRecordDurationIfExists() {
        Duration recordDuration = DURATION.plusHours(1);
        record = ImmutableRecord.copyOf(record).withDuration(recordDuration);

        garminStore.add(activity);
        recordStore.add(record);

        List<Workout> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasTotalDuration(recordDuration);
        assertThat(workout.getTotalDuration()).isNotEqualTo(DURATION);
    }

    @Test
    public void usesActivityDurationIfNoneInRecord() {
        record = ImmutableRecord.copyOf(record).withDuration(null);

        garminStore.add(activity);
        recordStore.add(record);

        List<Workout> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasTotalDuration(DURATION);
    }

    @Test
    public void useRecordDistanceIfExists() {
        Distance recordDistance = DISTANCE.multipliedBy(2);
        record = ImmutableRecord.copyOf(record).withDistance(recordDistance);

        garminStore.add(activity);
        recordStore.add(record);

        List<Workout> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasTotalDistance(recordDistance);
        assertThat(workout.getTotalDistance()).isNotEqualTo(DISTANCE);
    }

    @Test
    public void useActivityDistanceIfNoneInRecord() {
        record = ImmutableRecord.copyOf(record).withDistance(null);

        garminStore.add(activity);
        recordStore.add(record);

        List<Workout> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasTotalDistance(DISTANCE);
    }

    @Test
    public void combinesActivityDurationsIfMultipleMatches() {
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

        List<Workout> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasTotalDuration(expected);
    }

    @Test
    public void combinesActivityDistancesIfMultipleMatches() {
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

        List<Workout> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasTotalDistance(expected);
    }

    @Test
    public void combinesGoalDurationsIfMultipleMatches() throws Exception {
        List<Goal> goals = new ArrayList<>();
        int totalDuration = 0;
        for (long i = 1; i < 10; i++) {
            Goal multiple = ImmutableGoal.copyOf(goal)
                .withId(UUID.randomUUID())
                .withDuration(Duration.ofHours(i));
            goals.add(multiple);
            totalDuration += i;
        }
        Duration expected = Duration.ofHours(totalDuration);

        goalStore.addAll(goals);

        List<Workout> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasGoalDuration(expected);
    }

    @Test
    public void combinesGoalDistancesIfMultipleMatches() {
        List<Goal> goals = new ArrayList<>();
        double totalDistance = 0;
        for (long i = 1; i < 10; i++) {
            Distance dist = Distance.ofKilometers(i);
            Goal multiple = ImmutableGoal.copyOf(goal)
                .withId(UUID.randomUUID())
                .withDistance(dist);
            goals.add(multiple);
            totalDistance += i;
        }
        Distance expected = Distance.ofKilometers(totalDistance);

        goalStore.addAll(goals);

        List<Workout> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(1);

        Workout workout = workouts.get(0);
        WorkoutAssert.assertThat(workout).hasGoalDistance(expected);
    }

    //
//    @Test
//    public void useEarliestStartDateFromActivities() {
//
//    }
//
//    @Test
//    public void matchesMultipleTypesAtSameTime() {
//
//    }
//
//    @Test
//    public void usesActivityTypeMappingFile() {
//
//    }
//
//
//    @Test
//    public void getsAllWorkoutsByMultipleDates() {
//        garminStore.add(activity);
//        recordStore.add(record);
//
//        UUID newRecordId = UUID.randomUUID();
//        LocalDateTime newStart = START.plusDays(10);
//        LocalDate newDate = newStart.toLocalDate();
//        Record newRecord = ImmutableRecord.copyOf(record)
//            .withId(newRecordId)
//            .withDate(newDate);
//        recordStore.add(newRecord);
//
//        long newGarminId = activity.getId() + 100;
//        Activity newActivity = ImmutableActivity.copyOf(activity)
//            .withId(newGarminId)
//            .withStartTime(newStart);
//        garminStore.add(newActivity);
//
//        List<Workout> workouts = generateWorkoutStat.getAll();
//        assertThat(workouts).hasSize(2);
//
//        Workout workout = workouts.get(0);
//        WorkoutAssert.assertThat(workout).hasType(TYPE);
//        WorkoutAssert.assertThat(workout).hasDate(DATE);
//        WorkoutAssert.assertThat(workout).hasActivities(activity);
//        WorkoutAssert.assertThat(workout).hasRecord(record);
//        WorkoutAssert.assertThat(workout).hasTotalDuration(DURATION);
//        WorkoutAssert.assertThat(workout).hasTotalDistance(DISTANCE);
//
//        workout = workouts.get(1);
//        WorkoutAssert.assertThat(workout).hasType(TYPE);
//        WorkoutAssert.assertThat(workout).hasDate(newDate);
//        WorkoutAssert.assertThat(workout).hasRecord(newRecord);
//        WorkoutAssert.assertThat(workout).hasActivities(newActivity);
//        WorkoutAssert.assertThat(workout).hasTotalDuration(DURATION);
//        WorkoutAssert.assertThat(workout).hasTotalDistance(DISTANCE);
//    }
}
