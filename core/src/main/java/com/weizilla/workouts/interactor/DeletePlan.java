package com.weizilla.workouts.interactor;

import com.weizilla.workouts.store.PlanStore;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class DeletePlan {
    private final PlanStore store;

    @Inject
    public DeletePlan(PlanStore store) {
        this.store = store;
    }

    public void delete(UUID id) {
        store.delete(id);
    }
}
