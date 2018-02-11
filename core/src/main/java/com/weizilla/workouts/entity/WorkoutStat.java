package com.weizilla.workouts.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.weizilla.distance.Distance;
import org.immutables.value.Value.Default;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.immutables.value.Value.Immutable;

@Immutable
@JsonSerialize(as = ImmutableWorkoutStat.class)
@JsonDeserialize(as = ImmutableWorkoutStat.class)
@WorkoutsStyle
public interface WorkoutStat extends Entry<UUID> {

    @Default
    @Override
    default UUID getId() {
        return UUID.randomUUID();
    }

    List<Record> getRecords();

    List<Activity> getActivities();

    Completion getCompletion();

    Optional<Goal> getGoal();

    Optional<Distance> getTotalDistance();

    Optional<Duration> getTotalDuration();

    Optional<Distance> getGoalDistance();

    Optional<Duration> getGoalDuration();
}
