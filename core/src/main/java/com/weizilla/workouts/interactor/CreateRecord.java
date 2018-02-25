package com.weizilla.workouts.interactor;

import com.weizilla.distance.Distance;
import com.weizilla.workouts.entity.ImmutableRecord;
import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.store.RecordStore;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.util.Optional;

@Singleton
public class CreateRecord {
    private final RecordStore recordStore;

    @Inject
    public CreateRecord(RecordStore recordStore) {
        this.recordStore = recordStore;
    }

    public Record create(Record record) {
        Optional<Duration> duration = record.getDuration().filter(d -> d.getSeconds() > 0);
        Optional<Distance> distance = record.getDistance().filter(d -> d.getDistanceMeter() > 0);
        Record r = ImmutableRecord.copyOf(record)
            .withDuration(duration)
            .withDistance(distance);
        recordStore.add(r);
        return r;
    }
}
