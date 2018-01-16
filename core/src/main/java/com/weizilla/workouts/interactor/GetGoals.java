package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.store.GoalStore;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Singleton
public class GetGoals {
    private final GoalStore store;

    @Inject
    public GetGoals(GoalStore store) {
        this.store = store;
    }

    public Goal get(UUID id) {
        return store.get(id);
    }

    public List<Goal> get(LocalDate date, int numDays) {
        return IntStream.range(0, numDays)
            .mapToObj(date::plusDays)
            .flatMap(d -> store.get(d).stream())
            .collect(Collectors.toList());
    }

    public List<Goal> getAll() {
        return store.getAll();
    }
}
