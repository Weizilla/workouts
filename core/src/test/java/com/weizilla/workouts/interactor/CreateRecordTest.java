package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.entity.TestEntity;
import com.weizilla.workouts.store.RecordStore;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

public class CreateRecordTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    private RecordStore recordStore;
    private CreateRecord createRecord;
    private Record createRecordDto;

    @Before
    public void setUp() throws Exception {
        createRecordDto = TestEntity.createRecord();
        createRecord = new CreateRecord(recordStore);
    }

    @Test
    public void createShouldReturnNewRecordWithId() {
        Record actual = createRecord.create(createRecordDto);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(createRecordDto);
    }

    @Test
    public void createShouldStoreNewRecordInStore() throws Exception {
        Record actual = createRecord.create(createRecordDto);
        verify(recordStore).add(actual);
    }
}