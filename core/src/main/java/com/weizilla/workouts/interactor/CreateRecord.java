package com.weizilla.workouts.interactor;

import com.weizilla.workouts.dto.CreateDto;
import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.store.RecordStore;

import java.util.UUID;

public class CreateRecord {
    private final RecordStore recordStore;

    public CreateRecord(RecordStore recordStore) {
        this.recordStore = recordStore;
    }

    public Record create(CreateDto createDto) {
        UUID id = UUID.randomUUID();
        Record record = new Record(id, createDto.getType(), createDto.getDate(), createDto.getRating());
        createDto.getDuration().ifPresent(record::setDuration);
        createDto.getComment().ifPresent(record::setComment);
        recordStore.add(record);
        return record;
    }
}
