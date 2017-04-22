package com.weizilla.workouts.interactor;

import com.weizilla.workouts.dto.CreateDto;
import com.weizilla.workouts.entity.Record;

import java.util.UUID;

public class CreateRecord {
    public Record create(CreateDto createDto) {
        UUID id = UUID.randomUUID();
        Record record = new Record(id, createDto.getType(), createDto.getDate(), createDto.getRating());
        createDto.getDuration().ifPresent(record::setDuration);
        createDto.getComment().ifPresent(record::setComment);
        //TODO STORE
//        store.save(record);
        return record;
    }
}
