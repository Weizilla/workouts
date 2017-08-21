package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.Plan;
import com.weizilla.workouts.store.PlanStore;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UpdatePlan {
    private final PlanStore store;

    @Inject
    public UpdatePlan(PlanStore store) {
        this.store = store;
    }

    public void updatePlan(Plan plan) {
        store.update(plan);
    }
}
