package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.Record;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class GetRecordsTest extends RecordTest {
    private GetRecords getRecords;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        getRecords = new GetRecords(recordStore);
    }

    @Test
    public void getShouldReturnRecordById() throws Exception {
        when(recordStore.get(ID)).thenReturn(record);
        Record actual = getRecords.get(ID);
        assertThat(actual).isEqualTo(record);
    }

    @Test
    public void getShouldReturnNull() throws Exception {
        Record actual = getRecords.get(ID);
        assertThat(actual).isNull();
    }

    @Test
    public void getShouldReturnRecordByDate() throws Exception {
        when(recordStore.get(DATE)).thenReturn(Collections.singletonList(record));
        List<Record> actual = getRecords.get(DATE);
        assertThat(actual).containsExactly(record);
    }

    @Test
    public void getShouldReturnEmptyListIfNoRecordsFound() throws Exception {
        List<Record> actual = getRecords.get(DATE);
        assertThat(actual).isEmpty();
    }

    @Test
    public void getShouldReturnAllRecords() throws Exception {
        when(recordStore.getAll()).thenReturn(Collections.singletonList(record));
        List<Record> actual = getRecords.getAll();
        assertThat(actual).containsExactly(record);
    }
}