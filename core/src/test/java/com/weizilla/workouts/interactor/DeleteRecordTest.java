package com.weizilla.workouts.interactor;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.verify;

public class DeleteRecordTest extends RecordTest {
    private DeleteRecord deleteRecord;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        deleteRecord = new DeleteRecord(recordStore);
    }

    @Test
    public void deleteRecordShouldDeleteRecordFromStore() throws Exception {
        deleteRecord.delete(ID);
        verify(recordStore).delete(ID);
    }
}