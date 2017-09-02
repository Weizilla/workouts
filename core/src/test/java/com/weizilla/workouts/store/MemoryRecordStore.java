package com.weizilla.workouts.store;

import com.weizilla.workouts.entity.Record;

import java.util.UUID;

public class MemoryRecordStore extends MemoryEntryStore<UUID, Record> implements RecordStore {
}
