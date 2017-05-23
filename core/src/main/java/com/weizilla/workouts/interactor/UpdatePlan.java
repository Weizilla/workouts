package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.Plan;
import com.weizilla.workouts.store.PlanStore;

public class UpdatePlan {
    private final PlanStore store;

    public UpdatePlan(PlanStore store) {
        this.store = store;
    }

    public void updatePlan(Plan plan) {
        store.update(plan);
    }
}
