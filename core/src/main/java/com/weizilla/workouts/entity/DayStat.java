package com.weizilla.workouts.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value.Default;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.immutables.value.Value.Immutable;

@Immutable
@JsonSerialize(as = ImmutableDayStat.class)
@JsonDeserialize(as = ImmutableDayStat.class)
@WorkoutsStyle
public interface DayStat {

    LocalDate getDate();

    @Default
    default UUID getId() {
        return UUID.randomUUID();
    }

    List<WorkoutStat> getWorkoutStats();

    Completion getCompletion();
}
