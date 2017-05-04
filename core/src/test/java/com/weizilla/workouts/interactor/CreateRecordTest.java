package com.weizilla.workouts.interactor;

import com.weizilla.workouts.dto.CreateDto;
import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.entity.RecordAssert;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

public class CreateRecordTest extends RecordTest {
    private CreateRecord createRecord;
    private CreateDto createDto;

    @Before
    public void setUp() throws Exception {
        createDto = new CreateDto();
        createRecord = new CreateRecord(recordStore);
    }

    @Test
    public void createShouldReturnNewRecordWithId() {
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
    public void createShouldStoreNewRecordInStore() throws Exception {
        createDto.setType(TYPE);
        createDto.setDate(DATE);
        createDto.setRating(RATING);
        createDto.setComment(COMMENT);
        createDto.setDuration(DURATION);

        Record actual = createRecord.create(createDto);
        verify(recordStore).add(actual);
    }
}