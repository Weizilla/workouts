package com.weizilla.workouts.interactor;

import com.weizilla.workouts.dto.CreateRecordDto;
import com.weizilla.workouts.dto.ImmutableCreateRecordDto;
import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.entity.RecordAssert;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

public class CreateRecordTest extends RecordTest {
    private CreateRecord createRecord;
    private CreateRecordDto createRecordDto;

    @Before
    public void setUp() throws Exception {
        createRecordDto = ImmutableCreateRecordDto.builder()
            .type(TYPE)
            .date(DATE)
            .rating(RATING)
            .comment(COMMENT)
            .distance(DISTANCE)
            .duration(DURATION)
            .build();
        createRecord = new CreateRecord(recordStore);
    }

    @Test
    public void createShouldReturnNewRecordWithId() {
        Record actual = createRecord.create(createRecordDto);
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
        RecordAssert.assertThat(actual).hasType(TYPE);
        RecordAssert.assertThat(actual).hasDate(DATE);
        RecordAssert.assertThat(actual).hasRating(RATING);
        RecordAssert.assertThat(actual).hasDuration(DURATION);
        RecordAssert.assertThat(actual).hasComment(COMMENT);
        RecordAssert.assertThat(actual).hasDistance(DISTANCE);
    }

    @Test
    public void createShouldStoreNewRecordInStore() throws Exception {
        Record actual = createRecord.create(createRecordDto);
        verify(recordStore).add(actual);
    }
}