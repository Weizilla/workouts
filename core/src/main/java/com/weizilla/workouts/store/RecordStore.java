package com.weizilla.workouts.store;

import com.weizilla.workouts.entity.Record;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface RecordStore {
    void add(Record value);

    void addAll(Collection<Record> values);

    Record get(UUID id);

    List<Record> get(LocalDate date);

    List<Record> getAll();

    void update(Record value);

    void delete(UUID id);
}
