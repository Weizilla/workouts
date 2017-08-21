package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.Plan;
import com.weizilla.workouts.store.PlanStore;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Singleton
public class GetPlans {
    private final PlanStore store;

    @Inject
    public GetPlans(PlanStore store) {
        this.store = store;
    }

    public Plan get(UUID id) {
        return store.get(id);
    }

    public List<Plan> get(LocalDate date) {
        return store.get(date);
    }

    public List<Plan> getAll() {
        return store.getAll();
    }
}
