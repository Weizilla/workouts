package com.weizilla.workouts.interactor;

import com.weizilla.workouts.dto.CreateRecordDto;
import com.weizilla.workouts.entity.ImmutableRecord;
import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.store.RecordStore;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class CreateRecord {
    private final RecordStore recordStore;

    @Inject
    public CreateRecord(RecordStore recordStore) {
        this.recordStore = recordStore;
    }

    public Record create(CreateRecordDto createRecordDto) {
        Record record = ImmutableRecord.builder()
            .id(UUID.randomUUID())
            .type(createRecordDto.getType())
            .isOutdoor(createRecordDto.isOutdoor())
            .date(createRecordDto.getDate())
            .rating(createRecordDto.getRating())
            .duration(createRecordDto.getDuration())
            .distance(createRecordDto.getDistance())
            .comment(createRecordDto.getComment())
            .build();
        recordStore.add(record);
        return record;
    }
}
