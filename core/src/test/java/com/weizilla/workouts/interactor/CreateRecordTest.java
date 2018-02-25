package com.weizilla.workouts.interactor;

import com.weizilla.distance.Distance;
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

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

public class CreateRecordTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    private RecordStore recordStore;
    private CreateRecord createRecord;
    private Record record;

    @Before
    public void setUp() throws Exception {
        record = TestEntity.createRecord();
        createRecord = new CreateRecord(recordStore);
    }

    @Test
    public void createShouldReturnNewRecordWithId() {
        Record actual = createRecord.create(record);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(record);
    }

    @Test
    public void ignoresZeroDurationAndDistanceWhenAddingRecords() throws Exception {
        Record withZeros = ImmutableRecord.copyOf(record)
            .withDuration(Duration.ofSeconds(0))
            .withDistance(Distance.ofMeters(0));
        Record withEmpties = ImmutableRecord.copyOf(record)
            .withDistance(Optional.empty())
            .withDuration(Optional.empty());

        Record actual = createRecord.create(withZeros);
        assertThat(actual).isEqualTo(withEmpties);
        verify(recordStore).add(withEmpties);
    }

    @Test
    public void createShouldStoreNewRecordInStore() throws Exception {
        Record actual = createRecord.create(record);
        verify(recordStore).add(actual);
    }
}
