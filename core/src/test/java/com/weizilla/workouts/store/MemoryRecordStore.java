package com.weizilla.workouts.store;

import com.weizilla.workouts.entity.Record;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class MemoryRecordStore implements RecordStore {
    private final Map<UUID, Record> byId = new HashMap<>();

    @Override
    public void add(Record record) {
        byId.put(record.getId(), record);
    }

    @Override
    public Record get(UUID id) {
        return byId.get(id);
    }

    @Override
    public List<Record> getAll() {
        return byId.values().stream()
            .sorted(Comparator.comparing(Record::getDate))
            .collect(Collectors.toList());
    }

    @Override
    public List<Record> get(LocalDate date) {
        return byId.values().stream()
            .filter(a -> a.getDate().equals(date))
            .collect(Collectors.toList());
    }

    @Override
    public void update(Record record) {
        delete(record.getId());
    }

    @Override
    public void delete(UUID id) {
        byId.remove(id);
    }
}
