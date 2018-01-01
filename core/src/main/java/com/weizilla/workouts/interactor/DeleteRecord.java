package com.weizilla.workouts.interactor;

import com.weizilla.workouts.store.RecordStore;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class DeleteRecord {
    private final RecordStore store;

    @Inject
    public DeleteRecord(RecordStore store) {
        this.store = store;
    }

    public UUID delete(UUID id) {
        store.delete(id);
        return id;
    }
}
