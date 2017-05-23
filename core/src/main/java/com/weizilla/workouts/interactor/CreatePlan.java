package com.weizilla.workouts.interactor;

import com.weizilla.workouts.dto.CreatePlanDto;
import com.weizilla.workouts.entity.Plan;
import com.weizilla.workouts.store.PlanStore;

import java.util.UUID;

public class CreatePlan {
    private final PlanStore planStore;

    public CreatePlan(PlanStore planStore) {
        this.planStore = planStore;
    }

    public Plan create(CreatePlanDto createDto) {
        UUID id = UUID.randomUUID();
        Plan plan = new Plan(id, createDto.getType(), createDto.getDate(), createDto.getTimeOfDay());
        plan.setDistance(createDto.getDistance());
        plan.setNotes(createDto.getNotes());
        plan.setDuration(createDto.getDuration());
        planStore.add(plan);
        return plan;
    }
}
