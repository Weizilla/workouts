package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.store.RecordStore;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UpdateRecord {
    private final RecordStore store;

    @Inject
    public UpdateRecord(RecordStore store) {
        this.store = store;
    }

    public Record updateRecord(Record record) {
        store.update(record);
        return record;
    }
}
