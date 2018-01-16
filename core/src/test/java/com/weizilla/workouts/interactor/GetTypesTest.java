package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.entity.ImmutableGoal;
import com.weizilla.workouts.entity.ImmutableRecord;
import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.entity.TestEntity;
import com.weizilla.workouts.store.GoalStore;
import com.weizilla.workouts.store.MemoryGoalStore;
import com.weizilla.workouts.store.MemoryRecordStore;
import com.weizilla.workouts.store.RecordStore;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class GetTypesTest {
    private GoalStore goalStore;
    private RecordStore recordStore;
    private GetTypes getTypes;
    private Goal goal;
    private Record record;
    private String recordType;
    private String goalType;

    @Before
    public void setUp() throws Exception {
        goalStore = new MemoryGoalStore();
        recordStore = new MemoryRecordStore();
        getTypes = new GetTypes(goalStore, recordStore);
        goalType = "GOAL TYPE";
        recordType = "RECORD TYPE";
        goal = ImmutableGoal.copyOf(TestEntity.createGoal()).withType(goalType);
        record = ImmutableRecord.copyOf(TestEntity.createRecord()).withType(recordType);
    }

    @Test
    public void emptySetForEmptyStores() throws Exception {
        assertThat(getTypes.get()).isEmpty();
    }

    @Test
    public void getsAllTypesFromGoals() throws Exception {
        goalStore.add(goal);

        Set<String> types = getTypes.get();
        assertThat(types).containsExactly(goalType);
    }

    @Test
    public void getsAllTypesFromRecords() throws Exception {
        recordStore.add(record);

        Set<String> types = getTypes.get();
        assertThat(types).containsExactly(recordType);
    }

    @Test
    public void getsTypesFromGoalsAndRecords() throws Exception {
        goalStore.add(goal);
        recordStore.add(record);

        Set<String> types = getTypes.get();
        assertThat(types).containsExactly(goalType, recordType);
    }
}
