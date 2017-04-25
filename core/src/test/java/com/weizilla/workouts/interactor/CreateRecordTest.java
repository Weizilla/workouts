package com.weizilla.workouts.interactor;

import com.weizilla.workouts.dto.CreateDto;
import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.entity.RecordAssert;
import com.weizilla.workouts.store.RecordStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CreateRecordTest {
    private static final String TYPE = "TYPE";
    private static final LocalDate DATE = LocalDate.now();
    private static final int RATING = 3;
    private static final Duration DURATION = Duration.ofHours(1);
    private static final String COMMENT = "COMMENT";
    @Mock
    private RecordStore recordStore;
    private CreateRecord createRecord;
    private CreateDto createDto;

    @Before
    public void setUp() throws Exception {
        createDto = new CreateDto();
        createRecord = new CreateRecord(recordStore);
    }

    @Test
    public void createWillReturnNewRecordWithId() {
        createDto.setType(TYPE);
        createDto.setDate(DATE);
        createDto.setRating(RATING);
        createDto.setComment(COMMENT);
        createDto.setDuration(DURATION);

        Record actual = createRecord.create(createDto);
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
        RecordAssert.assertThat(actual).hasType(TYPE);
        RecordAssert.assertThat(actual).hasDate(DATE);
        RecordAssert.assertThat(actual).hasRating(RATING);
        RecordAssert.assertThat(actual).hasDuration(DURATION);
        RecordAssert.assertThat(actual).hasComment(COMMENT);
    }

    @Test
    public void createWillStoreNewRecord() throws Exception {
        createDto.setType(TYPE);
        createDto.setDate(DATE);
        createDto.setRating(RATING);
        createDto.setComment(COMMENT);
        createDto.setDuration(DURATION);

        Record actual = createRecord.create(createDto);
        verify(recordStore).add(actual);
    }
}