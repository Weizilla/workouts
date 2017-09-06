package com.weizilla.workouts.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.weizilla.distance.Distance;
import org.immutables.value.Value.Default;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static org.immutables.value.Value.Immutable;

@Immutable
@JsonSerialize(as = ImmutableWorkout.class)
@JsonDeserialize(as = ImmutableWorkout.class)
@WorkoutsStyle
public interface Workout extends Entry<UUID> {

    @Default
    @Override
    default UUID getId() {
        return UUID.randomUUID();
    }

    Record getRecord();

    List<Activity> getActivities();

    @Nullable
    Goal getGoal();

    @Nullable
    Distance getTotalDistance();

    @Nullable
    Duration getTotalDuration();

    @Nullable
    Distance getGoalDistance();

    @Nullable
    Duration getGoalDuration();
}
