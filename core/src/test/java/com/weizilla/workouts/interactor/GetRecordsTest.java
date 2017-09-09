package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.entity.TestEntity;
import com.weizilla.workouts.store.MemoryRecordStore;
import com.weizilla.workouts.store.RecordStore;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.weizilla.workouts.entity.TestEntity.DATE;
import static com.weizilla.workouts.entity.TestEntity.RECORD_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class GetRecordsTest {
    private RecordStore recordStore;
    private GetRecords getRecords;
    private Record record;

    @Before
    public void setUp() throws Exception {
        recordStore = new MemoryRecordStore();
        record = TestEntity.createRecord();
        getRecords = new GetRecords(recordStore);
    }

    @Test
    public void getShouldReturnRecordById() throws Exception {
        recordStore.add(record);
        Record actual = getRecords.get(RECORD_ID);
        assertThat(actual).isEqualTo(record);
    }

    @Test
    public void getShouldReturnNull() throws Exception {
        Record actual = getRecords.get(RECORD_ID);
        assertThat(actual).isNull();
    }

    @Test
    public void getShouldReturnRecordByDate() throws Exception {
        recordStore.add(record);
        List<Record> actual = getRecords.get(DATE);
        assertThat(actual).containsExactly(record);
    }

    @Test
    public void getShouldReturnEmptyListIfNoRecordsFound() throws Exception {
        List<Record> actual = getRecords.get(record.getDate());
        assertThat(actual).isEmpty();
    }

    @Test
    public void getShouldReturnAllRecords() throws Exception {
        recordStore.add(record);
        List<Record> actual = getRecords.getAll();
        assertThat(actual).containsExactly(record);
    }
}