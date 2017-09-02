package com.weizilla.workouts.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.weizilla.distance.Distance;
import org.immutables.value.Value.Default;

import javax.annotation.Nullable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.immutables.value.Value.Immutable;

@Immutable
@JsonSerialize(as = ImmutableWorkout.class)
@JsonDeserialize(as = ImmutableWorkout.class)
@WorkoutsStyle
public interface Workout extends Entry<UUID> {
    @Override
    @Default
    default UUID getId() {
        return UUID.randomUUID();
    }

    @Default
    @Override
    default LocalDate getDate() {
        return getStartTime().toLocalDate();
    }

    // from Record
    UUID getRecordId();

    int getRating();

    @Nullable
    String getComment();

    // from Garmin activity

    LocalDateTime getStartTime();

    List<Long> getGarminIds();

    // from Goal

    @Nullable
    UUID getGoalId();

    @Nullable
    Distance getGoalDistance();

    @Nullable
    Duration getGoalDuration();

    @Nullable
    TimeOfDay getTimeOfDay();

    @Nullable
    String getNotes();

}
