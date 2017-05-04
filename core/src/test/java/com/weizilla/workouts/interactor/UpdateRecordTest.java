package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.Record;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;

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
        Record newRecord = new Record(ID, "NEW TYPE", DATE.plusDays(1), 5);
        newRecord.setComment("NEW COMMENT");
        newRecord.setDuration(Duration.ofMinutes(1));

        updateRecord.updateRecord(record);
        verify(recordStore).update(record);
    }

}