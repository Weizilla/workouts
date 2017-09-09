package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.ImmutableRecord;
import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.entity.TestEntity;
import com.weizilla.workouts.store.RecordStore;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static com.weizilla.workouts.entity.TestEntity.DATE;
import static com.weizilla.workouts.entity.TestEntity.DISTANCE;
import static com.weizilla.workouts.entity.TestEntity.DURATION;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class UpdateRecordTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    private RecordStore recordStore;
    private UpdateRecord updateRecord;
    private Record record;

    @Before
    public void setUp() throws Exception {
        record = TestEntity.createRecord();
        updateRecord = new UpdateRecord(recordStore);
    }

    @Test
    public void updatesShouldUpdateRecordWithNewRecord() throws Exception {
        Record newRecord = ImmutableRecord.copyOf(record)
            .withType("NEW TYPE")
            .withOutdoor(false)
            .withDate(DATE.plusDays(1))
            .withRating(5)
            .withComment("NEW COMMENT")
            .withDuration(DURATION.plusHours(2))
            .withDistance(DISTANCE.multipliedBy(2));

        updateRecord.updateRecord(newRecord);
        verify(recordStore).update(newRecord);
        verify(recordStore, never()).update(record);
    }

}