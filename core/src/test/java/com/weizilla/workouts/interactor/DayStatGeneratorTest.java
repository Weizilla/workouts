package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.Activity;
import com.weizilla.workouts.entity.Completion;
import com.weizilla.workouts.entity.DayStat;
import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.entity.ImmutableGoal;
import com.weizilla.workouts.entity.ImmutableRecord;
import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.entity.TestEntity;
import com.weizilla.workouts.store.GarminStore;
import com.weizilla.workouts.store.GoalStore;
import com.weizilla.workouts.store.MemoryGarminStore;
import com.weizilla.workouts.store.MemoryGoalStore;
import com.weizilla.workouts.store.MemoryRecordStore;
import com.weizilla.workouts.store.RecordStore;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static com.weizilla.workouts.entity.TestEntity.DATE;
import static org.assertj.core.api.Assertions.assertThat;

//TODO combine with Workout Stat
public class DayStatGeneratorTest {
    private RecordStore recordStore;
    private GarminStore garminStore;
    private GoalStore goalStore;
    private WorkoutStatGenerator generator;
    private Activity activity;
    private Record record;
    private Goal goal;
    private CompletionCalculator completionCalculator;

    @Before
    public void setUp() {
        garminStore = new MemoryGarminStore();
        recordStore = new MemoryRecordStore();
        goalStore = new MemoryGoalStore();
        activity = TestEntity.createActivity();
        record = TestEntity.createRecord();
        goal = TestEntity.createGoal();
        completionCalculator = new CompletionCalculator();

        generator = new WorkoutStatGenerator(recordStore, garminStore, goalStore, completionCalculator);
    }

    @Test
    public void useWorkoutCompletionForDayStatIfAllAreSameForAll() throws Exception {
        recordStore.add(record);
        Record type2 = ImmutableRecord.copyOf(record).withId(UUID.randomUUID()).withType("type 2");
        recordStore.add(type2);

        DayStat generate = generator.generate(DATE).get();
        assertThat(generate.getCompletion()).isEqualTo(Completion.ALL);
        assertThat(generate.getWorkoutStats().get(0).getCompletion()).isEqualTo(Completion.ALL);
        assertThat(generate.getWorkoutStats().get(1).getCompletion()).isEqualTo(Completion.ALL);
    }

    @Test
    public void useWorkoutCompletionForDayStatIfAllAreSameForNone() throws Exception {
        goalStore.add(goal);
        Goal type2 = ImmutableGoal.copyOf(goal).withId(UUID.randomUUID()).withType("type 2");
        goalStore.add(type2);

        DayStat generate = generator.generate(DATE).get();
        assertThat(generate.getCompletion()).isEqualTo(Completion.NONE);
        assertThat(generate.getWorkoutStats().get(0).getCompletion()).isEqualTo(Completion.NONE);
        assertThat(generate.getWorkoutStats().get(1).getCompletion()).isEqualTo(Completion.NONE);
    }

    @Test
    public void dayStatHasSomeCompletionIfWorkoutCompletionIsDifferent() throws Exception {
        recordStore.add(record);
        Goal type2 = ImmutableGoal.copyOf(goal).withId(UUID.randomUUID()).withType("type 2");
        goalStore.add(type2);

        DayStat generate = generator.generate(DATE).get();
        assertThat(generate.getCompletion()).isEqualTo(Completion.SOME);
        assertThat(generate.getWorkoutStats().get(0).getCompletion()).isEqualTo(Completion.ALL);
        assertThat(generate.getWorkoutStats().get(1).getCompletion()).isEqualTo(Completion.NONE);

    }

}
