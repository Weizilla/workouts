package com.weizilla.workouts.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.weizilla.distance.Distance;
import org.immutables.value.Value.Default;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
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

    @Nullable
    Record getRecord();

    List<Activity> getActivities();

    Optional<Goal> getGoal();

    Optional<Distance> getTotalDistance();

    Optional<Duration> getTotalDuration();

    Optional<Distance> getGoalDistance();

    Optional<Duration> getGoalDuration();
}
