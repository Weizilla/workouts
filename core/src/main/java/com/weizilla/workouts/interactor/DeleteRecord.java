package com.weizilla.workouts.interactor;

import com.weizilla.workouts.store.RecordStore;

import java.util.UUID;

public class DeleteRecord {
    private final RecordStore store;

    public DeleteRecord(RecordStore store) {
        this.store = store;
    }

    public void delete(UUID id) {
        store.delete(id);
    }
}
