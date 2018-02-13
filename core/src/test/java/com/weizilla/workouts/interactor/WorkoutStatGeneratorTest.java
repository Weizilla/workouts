package com.weizilla.workouts.interactor;

import com.weizilla.distance.Distance;
import com.weizilla.workouts.entity.Activity;
import com.weizilla.workouts.entity.Completion;
import com.weizilla.workouts.entity.DayStat;
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
import java.util.Optional;
import java.util.UUID;

import static com.weizilla.workouts.entity.TestEntity.DATE;
import static com.weizilla.workouts.entity.TestEntity.DISTANCE;
import static com.weizilla.workouts.entity.TestEntity.DURATION;
import static com.weizilla.workouts.entity.TestEntity.TYPE;
import static org.assertj.core.api.Assertions.assertThat;

public class WorkoutStatGeneratorTest {
    private RecordStore recordStore;
    private GarminStore garminStore;
    private GoalStore goalStore;
    private WorkoutStatGenerator generator;
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

        generator = new WorkoutStatGenerator(recordStore, garminStore, goalStore, completionCalculator);
    }

    @Test
    public void returnsEmptyListWithNothing() {
        assertThat(generator.generate(DATE)).isEmpty();
        assertThat(generator.generateAll()).isEmpty();
    }

    @Test
    public void returnsActivityForGivenDate() {
        garminStore.add(activity);

        List<WorkoutStat> workouts = generate(DATE);
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

        List<WorkoutStat> workouts = generate(DATE);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasType(TYPE);
        WorkoutStatAssert.assertThat(workout).hasDate(DATE);
        WorkoutStatAssert.assertThat(workout).hasOnlyActivities(activity, activity2);
    }

    @Test
    public void returnsRecordsForGivenDate() {
        recordStore.add(record);

        List<WorkoutStat> workouts = generate(DATE);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasType(TYPE);
        WorkoutStatAssert.assertThat(workout).hasDate(DATE);
        WorkoutStatAssert.assertThat(workout).hasOnlyRecords(record);
    }

    @Test
    public void returnsGoalForGivenDate() {
        goalStore.add(goal);

        List<WorkoutStat> workouts = generate(DATE);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasType(TYPE);
        WorkoutStatAssert.assertThat(workout).hasDate(DATE);
        WorkoutStatAssert.assertThat(workout).hasGoal(Optional.of(goal));
    }

    @Test
    public void returnsMatchedWorkout() {
        garminStore.add(activity);
        recordStore.add(record);
        goalStore.add(goal);

        List<WorkoutStat> workouts = generate(DATE);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasType(TYPE);
        WorkoutStatAssert.assertThat(workout).hasDate(DATE);
        WorkoutStatAssert.assertThat(workout).hasOnlyRecords(record);
        WorkoutStatAssert.assertThat(workout).hasOnlyActivities(activity);
        WorkoutStatAssert.assertThat(workout).hasGoal(Optional.of(goal));
    }

    @Test
    public void returnsMultipleWorkoutsForDifferentTypes() throws Exception {
        Record newType = ImmutableRecord.copyOf(record).withType("NEW TYPE");
        garminStore.add(activity);
        recordStore.add(newType);
        goalStore.add(goal);

        List<WorkoutStat> workouts = generate(DATE);
        assertThat(workouts).hasSize(2);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasType("NEW TYPE");
        WorkoutStatAssert.assertThat(workout).hasDate(DATE);
        WorkoutStatAssert.assertThat(workout).hasOnlyRecords(newType);
        WorkoutStatAssert.assertThat(workout).hasNoActivities();
        WorkoutStatAssert.assertThat(workout).hasGoal(Optional.empty());

        WorkoutStat workout2 = workouts.get(1);
        WorkoutStatAssert.assertThat(workout2).hasType(TYPE);
        WorkoutStatAssert.assertThat(workout2).hasDate(DATE);
        WorkoutStatAssert.assertThat(workout2).hasNoRecords();
        WorkoutStatAssert.assertThat(workout2).hasOnlyActivities(activity);
        WorkoutStatAssert.assertThat(workout2).hasGoal(Optional.of(goal));
    }

    @Test
    public void returnsMultipleWorkoutsForDifferentDates() throws Exception {
        LocalDate newDate = DATE.plusDays(1);
        Record newRecord = ImmutableRecord.copyOf(record).withDate(newDate);
        garminStore.add(activity);
        recordStore.add(newRecord);
        goalStore.add(goal);

        List<DayStat> dayStats = generator.generateAll();
        assertThat(dayStats).hasSize(2);

        WorkoutStat workout = dayStats.get(0).getWorkoutStats().get(0);
        WorkoutStatAssert.assertThat(workout).hasType(TYPE);
        WorkoutStatAssert.assertThat(workout).hasDate(DATE);
        WorkoutStatAssert.assertThat(workout).hasNoRecords();
        WorkoutStatAssert.assertThat(workout).hasOnlyActivities(activity);
        WorkoutStatAssert.assertThat(workout).hasGoal(Optional.of(goal));

        WorkoutStat workout2 = dayStats.get(1).getWorkoutStats().get(0);
        WorkoutStatAssert.assertThat(workout2).hasType(TYPE);
        WorkoutStatAssert.assertThat(workout2).hasDate(newDate);
        WorkoutStatAssert.assertThat(workout2).hasOnlyRecords(newRecord);
        WorkoutStatAssert.assertThat(workout2).hasNoActivities();
        WorkoutStatAssert.assertThat(workout2).hasGoal(Optional.empty());
    }

    @Test
    public void usesRecordDurationIfExists() {
        Duration recordDuration = DURATION.plusHours(1);
        record = ImmutableRecord.copyOf(record).withDuration(recordDuration);

        garminStore.add(activity);
        recordStore.add(record);

        List<WorkoutStat> workouts = generate(DATE);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasTotalDuration(Optional.of(recordDuration));
        assertThat(workout.getTotalDuration()).isNotEqualTo(DURATION);
    }

    @Test
    public void usesActivityDurationIfNoneInRecord() {
        record = ImmutableRecord.copyOf(record).withDuration(Optional.empty());

        garminStore.add(activity);
        recordStore.add(record);

        List<WorkoutStat> workouts = generate(DATE);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasTotalDuration(Optional.of(DURATION));
    }

    @Test
    public void useRecordDistanceIfExists() {
        Distance recordDistance = DISTANCE.multipliedBy(2);
        record = ImmutableRecord.copyOf(record).withDistance(recordDistance);

        garminStore.add(activity);
        recordStore.add(record);

        List<WorkoutStat> workouts = generate(DATE);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasTotalDistance(Optional.of(recordDistance));
        assertThat(workout.getTotalDistance()).isNotEqualTo(DISTANCE);
    }

    @Test
    public void useActivityDistanceIfNoneInRecord() {
        record = ImmutableRecord.copyOf(record).withDistance(Optional.empty());

        garminStore.add(activity);
        recordStore.add(record);

        List<WorkoutStat> workouts = generate(DATE);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasTotalDistance(Optional.of(DISTANCE));
    }

    @Test
    public void combinesRecordDurationsIfMultipleMatches() {
        List<Record> records = new ArrayList<>();
        int totalDuration = 0;
        for (long i = 1; i < 10; i++) {
            Record multiple = ImmutableRecord.copyOf(record)
                .withId(UUID.randomUUID())
                .withDuration(Duration.ofHours(i));
            records.add(multiple);
            totalDuration += i;
        }
        Duration expected = Duration.ofHours(totalDuration);

        recordStore.addAll(records);

        List<WorkoutStat> workouts = generate(DATE);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasTotalDuration(Optional.of(expected));
    }

    @Test
    public void combinesRecordDistancesIfMultipleMatches() {
        List<Record> records = new ArrayList<>();
        double totalDistance = 0;
        for (long i = 1; i < 10; i++) {
            Distance dist = Distance.ofKilometers(i);
            Record multiple = ImmutableRecord.copyOf(record)
                .withId(UUID.randomUUID())
                .withDistance(dist);
            records.add(multiple);
            totalDistance += i;
        }
        Distance expected = Distance.ofKilometers(totalDistance);

        recordStore.addAll(records);

        List<WorkoutStat> workouts = generate(DATE);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasTotalDistance(Optional.of(expected));
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

        List<WorkoutStat> workouts = generate(DATE);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasTotalDuration(Optional.of(expected));
    }

    @Test
    public void combinesActivityDistancesIfMultipleMatches() {
        List<Activity> activities = new ArrayList<>();
        double totalDistance = 0;
        for (long i = 1; i < 10; i++) {
            Distance dist = Distance.ofKilometers(i);
            Activity multiple = ImmutableActivity.copyOf(activity)
                .withId(i)
                .withDistance(dist);
            activities.add(multiple);
            totalDistance += i;
        }
        Distance expected = Distance.ofKilometers(totalDistance);

        garminStore.addAll(activities);

        List<WorkoutStat> workouts = generate(DATE);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasTotalDistance(Optional.of(expected));
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

        List<WorkoutStat> workouts = generate(DATE);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasGoalDuration(Optional.of(expected));
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

        List<WorkoutStat> workouts = generate(DATE);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasGoalDistance(Optional.of(expected));
    }


    @Test
    public void completionIsGoalIfGoalIsInFuture() throws Exception {
        LocalDate future = LocalDate.now().plusDays(1);
        Record futureRecord = ImmutableRecord.copyOf(record).withDate(future);
        recordStore.add(futureRecord);
        assertWorkoutCompletion(future, Completion.GOAL);
    }


    @Test
    public void completionIsAllIfNoGoalWithDuration() throws Exception {
        Record durationOnly = ImmutableRecord.copyOf(record).withDuration(Optional.empty());
        recordStore.add(durationOnly);
        assertThat(completion).isEqualTo(Completion.ALL);
    }

    @Test
    public void completionIsAllIfNoGoalWithDistance() throws Exception {
        Record distanceOnly = ImmutableRecord.copyOf(record).withDistance(Optional.empty());
        recordStore.add(distanceOnly);
        assertThat(completion).isEqualTo(Completion.ALL);
    }

    @Test
    public void completionIsAllIfNoGoalDistanceOrDuration() throws Exception {
        Record noOptionals = ImmutableRecord.copyOf(record)
            .withDuration(Optional.empty())
            .withDistance(Optional.empty());
        recordStore.add(noOptionals);
        assertThat(completion).isEqualTo(Completion.ALL);
    }

    @Test
    public void usesCompletionFromCalculator() throws Exception {
        goalStore.add(goal);
        recordStore.add(record);

        assertWorkoutCompletion(DATE, completion);
    }

    private void assertWorkoutCompletion(LocalDate date, Completion completion) {
        List<WorkoutStat> workouts = generate(date);
        assertThat(workouts).hasSize(1);

        WorkoutStat workout = workouts.get(0);
        WorkoutStatAssert.assertThat(workout).hasCompletion(completion);
    }

    private List<WorkoutStat> generate(LocalDate date) {
        return generator.generate(date).get().getWorkoutStats();
    }
}
