package com.weizilla.workouts.interactor;

import com.weizilla.workouts.store.RecordStore;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static com.weizilla.workouts.entity.TestEntity.RECORD_ID;
import static org.mockito.Mockito.verify;

public class DeleteRecordTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    private RecordStore recordStore;
    private DeleteRecord deleteRecord;

    @Before
    public void setUp() throws Exception {
        deleteRecord = new DeleteRecord(recordStore);
    }

    @Test
    public void deleteRecordShouldDeleteRecordFromStore() throws Exception {
        deleteRecord.delete(RECORD_ID);
        verify(recordStore).delete(RECORD_ID);
    }
}