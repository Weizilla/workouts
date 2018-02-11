package com.weizilla.workouts.interactor;

import com.weizilla.distance.Distance;
import com.weizilla.workouts.entity.Activity;
import com.weizilla.workouts.entity.Completion;
import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.entity.ImmutableActivity;
import com.weizilla.workouts.entity.ImmutableGoal;
import com.weizilla.workouts.entity.ImmutableRecord;
import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.entity.TestEntity;
import com.weizilla.workouts.entity.WorkoutStat;
import com.weizilla.workouts.entity.WorkoutStatAssert;
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
    private CompletionCalculator completionCalculator;
    private Completion completion;

    @Before
    public void setUp() {
        garminStore = new MemoryGarminStore();
        recordStore = new MemoryRecordStore();
        goalStore = new MemoryGoalStore();
        activity = TestEntity.createActivity();
        record = TestEntity.createRecord();
        goal = TestEntity.createGoal();
        completion = Completion.ALL;
        completionCalculator = new CompletionCalculatorStub(completion);

        generateWorkoutStat = new GenerateWorkoutStat(recordStore, garminStore, goalStore, completionCalculator);
    }

    @Test
    public void returnsEmptyListWithNothing() {
        assertThat(generateWorkoutStat.get(DATE)).isEmpty();
        assertThat(generateWorkoutStat.getAll()).isEmpty();
    }

    @Test
    public void returnsActivityForGivenDate() {
        garminStore.add(activity);

        List<WorkoutStat> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasType(TYPE);
        WorkoutStatAssert.assertThat(workout).hasDate(DATE);
        WorkoutStatAssert.assertThat(workout).hasOnlyActivities(activity);
    }

    @Test
    public void returnsAllActivityForGivenDate() {
        garminStore.add(activity);
        Activity activity2 = TestEntity.createActivity();
        garminStore.add(activity2);

        List<WorkoutStat> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasType(TYPE);
        WorkoutStatAssert.assertThat(workout).hasDate(DATE);
        WorkoutStatAssert.assertThat(workout).hasOnlyActivities(activity, activity2);
    }

    @Test
    public void returnsRecordsForGivenDate() {
        recordStore.add(record);

        List<WorkoutStat> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasType(TYPE);
        WorkoutStatAssert.assertThat(workout).hasDate(DATE);
        WorkoutStatAssert.assertThat(workout).hasRecord(record);
    }

    @Test
    public void returnsGoalForGivenDate() {
        goalStore.add(goal);

        List<WorkoutStat> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasType(TYPE);
        WorkoutStatAssert.assertThat(workout).hasDate(DATE);
        WorkoutStatAssert.assertThat(workout).hasGoal(goal);
    }

    @Test
    public void returnsMatchedWorkout() {
        garminStore.add(activity);
        recordStore.add(record);
        goalStore.add(goal);

        List<WorkoutStat> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasType(TYPE);
        WorkoutStatAssert.assertThat(workout).hasDate(DATE);
        WorkoutStatAssert.assertThat(workout).hasRecord(record);
        WorkoutStatAssert.assertThat(workout).hasOnlyActivities(activity);
        WorkoutStatAssert.assertThat(workout).hasGoal(goal);
    }

    @Test
    public void returnsMultipleWorkoutsForDifferentTypes() throws Exception {
        Record newType = ImmutableRecord.copyOf(record).withType("NEW TYPE");
        garminStore.add(activity);
        recordStore.add(newType);
        goalStore.add(goal);

        List<WorkoutStat> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(2);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasType("NEW TYPE");
        WorkoutStatAssert.assertThat(workout).hasDate(DATE);
        WorkoutStatAssert.assertThat(workout).hasRecord(newType);
        WorkoutStatAssert.assertThat(workout).hasNoActivities();
        WorkoutStatAssert.assertThat(workout).hasGoal(null);

        WorkoutStat workout2 = workouts.get(1);
        WorkoutStatAssert.assertThat(workout2).hasType(TYPE);
        WorkoutStatAssert.assertThat(workout2).hasDate(DATE);
        WorkoutStatAssert.assertThat(workout2).hasRecord(null);
        WorkoutStatAssert.assertThat(workout2).hasOnlyActivities(activity);
        WorkoutStatAssert.assertThat(workout2).hasGoal(goal);
    }

    @Test
    public void returnsMultipleWorkoutsForDifferentDates() throws Exception {
        LocalDate newDate = DATE.plusDays(1);
        Record newRecord = ImmutableRecord.copyOf(record).withDate(newDate);
        garminStore.add(activity);
        recordStore.add(newRecord);
        goalStore.add(goal);

        List<WorkoutStat> workouts = generateWorkoutStat.getAll();
        assertThat(workouts).hasSize(2);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasType(TYPE);
        WorkoutStatAssert.assertThat(workout).hasDate(DATE);
        WorkoutStatAssert.assertThat(workout).hasRecord(null);
        WorkoutStatAssert.assertThat(workout).hasOnlyActivities(activity);
        WorkoutStatAssert.assertThat(workout).hasGoal(goal);

        WorkoutStat workout2 = workouts.get(1);
        WorkoutStatAssert.assertThat(workout2).hasType(TYPE);
        WorkoutStatAssert.assertThat(workout2).hasDate(newDate);
        WorkoutStatAssert.assertThat(workout2).hasRecord(newRecord);
        WorkoutStatAssert.assertThat(workout2).hasNoActivities();
        WorkoutStatAssert.assertThat(workout2).hasGoal(null);
    }

    @Test
    public void usesRecordDurationIfExists() {
        Duration recordDuration = DURATION.plusHours(1);
        record = ImmutableRecord.copyOf(record).withDuration(recordDuration);

        garminStore.add(activity);
        recordStore.add(record);

        List<WorkoutStat> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasTotalDuration(recordDuration);
        assertThat(workout.getTotalDuration()).isNotEqualTo(DURATION);
    }

    @Test
    public void usesActivityDurationIfNoneInRecord() {
        record = ImmutableRecord.copyOf(record).withDuration(null);

        garminStore.add(activity);
        recordStore.add(record);

        List<WorkoutStat> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasTotalDuration(DURATION);
    }

    @Test
    public void useRecordDistanceIfExists() {
        Distance recordDistance = DISTANCE.multipliedBy(2);
        record = ImmutableRecord.copyOf(record).withDistance(recordDistance);

        garminStore.add(activity);
        recordStore.add(record);

        List<WorkoutStat> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasTotalDistance(recordDistance);
        assertThat(workout.getTotalDistance()).isNotEqualTo(DISTANCE);
    }

    @Test
    public void useActivityDistanceIfNoneInRecord() {
        record = ImmutableRecord.copyOf(record).withDistance(null);

        garminStore.add(activity);
        recordStore.add(record);

        List<WorkoutStat> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasTotalDistance(DISTANCE);
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

        List<WorkoutStat> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasTotalDuration(expected);
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

        List<WorkoutStat> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasTotalDistance(expected);
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

        List<WorkoutStat> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasGoalDuration(expected);
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

        List<WorkoutStat> workouts = generateWorkoutStat.get(DATE);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasGoalDistance(expected);
    }

    @Test
    public void usesCompletionFromCalculator() throws Exception {

        goalStore.add(goal);
        recordStore.add(record);

        assertCompletion(DATE, completion);
    }

    private void assertCompletion(LocalDate date, Completion completion) {
        List<WorkoutStat> workouts = generateWorkoutStat.get(date);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasCompletion(completion);
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
}
