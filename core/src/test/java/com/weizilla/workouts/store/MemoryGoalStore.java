package com.weizilla.workouts.store;

import com.weizilla.workouts.entity.Goal;

import java.util.UUID;

public class MemoryGoalStore extends MemoryEntryStore<UUID, Goal> implements GoalStore {
}
