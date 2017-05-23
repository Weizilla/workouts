package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.ImmutableRecord;
import com.weizilla.workouts.entity.Record;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class UpdateRecordTest extends RecordTest {
    private UpdateRecord updateRecord;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        updateRecord = new UpdateRecord(recordStore);
    }

    @Test
    public void updatesShouldUpdateRecordWithNewRecord() throws Exception {
        Record newRecord = ImmutableRecord.copyOf(record)
            .withType("NEW TYPE")
            .withDate(DATE.plusDays(1))
            .withRating(5)
            .withComment("NEW COMMENT")
            .withDuration(DURATION.plusHours(2))
            .withDistance(DISTANCE.multiply(2));

        updateRecord.updateRecord(newRecord);
        verify(recordStore).update(newRecord);
        verify(recordStore, never()).update(record);
    }

}