package com.weizilla.workouts.store;

import com.weizilla.workouts.entity.Record;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RecordStore {
    void add(Record record);
    Record get(UUID id);
    List<Record> getAll();
    List<Record> get(LocalDate date);
    void update(Record record);
    void delete(UUID id);
}
