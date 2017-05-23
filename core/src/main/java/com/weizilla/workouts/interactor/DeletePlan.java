package com.weizilla.workouts.interactor;

import com.weizilla.workouts.store.PlanStore;

import java.util.UUID;

public class DeletePlan {
    private final PlanStore store;

    public DeletePlan(PlanStore store) {
        this.store = store;
    }

    public void delete(UUID id) {
        store.delete(id);
    }
}
