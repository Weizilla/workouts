package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.store.RecordStore;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CreateRecord {
    private final RecordStore recordStore;

    @Inject
    public CreateRecord(RecordStore recordStore) {
        this.recordStore = recordStore;
    }

    public Record create(Record record) {
        recordStore.add(record);
        return record;
    }
}
