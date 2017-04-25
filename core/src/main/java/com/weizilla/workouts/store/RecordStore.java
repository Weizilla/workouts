package com.weizilla.workouts.store;

import com.weizilla.workouts.entity.Record;

import java.util.UUID;

public interface RecordStore {
    void add(Record record);
    Record get(UUID id);
}
