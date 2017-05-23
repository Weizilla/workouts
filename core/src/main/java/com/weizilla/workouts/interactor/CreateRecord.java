package com.weizilla.workouts.interactor;

import com.weizilla.workouts.dto.CreateRecordDto;
import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.store.RecordStore;

import java.util.UUID;

public class CreateRecord {
    private final RecordStore recordStore;

    public CreateRecord(RecordStore recordStore) {
        this.recordStore = recordStore;
    }

    public Record create(CreateRecordDto createRecordDto) {
        UUID id = UUID.randomUUID();
        Record record = new Record(id, createRecordDto.getType(), createRecordDto.getDate(), createRecordDto
            .getRating());
        record.setDuration(createRecordDto.getDuration());
        record.setDistance(createRecordDto.getDistance());
        record.setComment(createRecordDto.getComment());
        recordStore.add(record);
        return record;
    }
}
