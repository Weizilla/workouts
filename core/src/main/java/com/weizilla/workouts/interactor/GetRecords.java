package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.store.RecordStore;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Singleton
public class GetRecords {
    private final RecordStore store;

    @Inject
    public GetRecords(RecordStore store) {
        this.store = store;
    }

    public Record get(UUID id) {
        return store.get(id);
    }

    public List<Record> get(LocalDate date) {
        return store.get(date);
    }

    public List<Record> getAll() {
        return store.getAll();
    }
}
