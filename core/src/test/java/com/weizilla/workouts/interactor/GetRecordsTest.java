package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.store.RecordStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetRecordsTest {
    private GetRecords getRecords;
    @Mock
    private RecordStore recordStore;
    private Record record;
    private UUID id;
    private String type;
    private LocalDate date;
    private int rating;

    @Before
    public void setUp() throws Exception {
        getRecords = new GetRecords(recordStore);
        id = UUID.randomUUID();
        type = "TYPE";
        date = LocalDate.now();
        rating = 3;
        record = new Record(id, type, date, rating);
    }

    @Test
    public void getsRecordByUuid() throws Exception {
        when(recordStore.get(id)).thenReturn(record);
        Record actual = getRecords.get(id);
        assertThat(actual).isEqualTo(record);
    }

    @Test
    public void getsAllRecordsByDate() throws Exception {
        when(recordStore.get(date)).thenReturn(Collections.singletonList(record));
        List<Record> actual = getRecords.get(date);
        assertThat(actual).containsExactly(record);
    }
}