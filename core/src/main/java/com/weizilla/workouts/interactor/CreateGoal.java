package com.weizilla.workouts.interactor;

import com.weizilla.workouts.dto.CreateGoalDto;
import com.weizilla.workouts.entity.ImmutableGoal;
import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.store.GoalStore;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class CreateGoal {
    private final GoalStore goalStore;

    @Inject
    public CreateGoal(GoalStore goalStore) {
        this.goalStore = goalStore;
    }

    public Goal create(CreateGoalDto createDto) {
        Goal goal = ImmutableGoal.builder()
            .id(UUID.randomUUID())
            .type(createDto.getType())
            .date(createDto.getDate())
            .timeOfDay(createDto.getTimeOfDay())
            .notes(createDto.getNotes())
            .duration(createDto.getDuration())
            .distance(createDto.getDistance())
            .build();
        goalStore.add(goal);
        return goal;
    }
}
