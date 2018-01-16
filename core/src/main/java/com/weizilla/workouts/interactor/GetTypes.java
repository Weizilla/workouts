package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.store.GoalStore;
import com.weizilla.workouts.store.RecordStore;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;
import java.util.TreeSet;

@Singleton
public class GetTypes {
    private final GoalStore goalStore;
    private final RecordStore recordStore;

    @Inject
    public GetTypes(GoalStore goalStore, RecordStore recordStore) {
        this.goalStore = goalStore;
        this.recordStore = recordStore;
    }

    public Set<String> get() {
        Set<String> types = new TreeSet<>();
        goalStore.getAll().stream()
            .map(Goal::getType)
            .forEach(types::add);
        recordStore.getAll().stream()
            .map(Record::getType)
            .forEach(types::add);
        return types;
    }
}
