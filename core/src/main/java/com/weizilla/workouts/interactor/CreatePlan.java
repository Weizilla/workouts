package com.weizilla.workouts.interactor;

import com.weizilla.workouts.dto.CreatePlanDto;
import com.weizilla.workouts.entity.ImmutablePlan;
import com.weizilla.workouts.entity.Plan;
import com.weizilla.workouts.store.PlanStore;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class CreatePlan {
    private final PlanStore planStore;

    @Inject
    public CreatePlan(PlanStore planStore) {
        this.planStore = planStore;
    }

    public Plan create(CreatePlanDto createDto) {
        Plan plan = ImmutablePlan.builder()
            .id(UUID.randomUUID())
            .type(createDto.getType())
            .date(createDto.getDate())
            .timeOfDay(createDto.getTimeOfDay())
            .notes(createDto.getNotes())
            .duration(createDto.getDuration())
            .distance(createDto.getDistance())
            .build();
        planStore.add(plan);
        return plan;
    }
}
