package com.weizilla.workouts.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDate;
import java.util.List;

import static org.immutables.value.Value.Immutable;

@Immutable
@JsonSerialize(as = ImmutableDayStat.class)
@JsonDeserialize(as = ImmutableDayStat.class)
@WorkoutsStyle
public interface DayStat {

    LocalDate getDate();

    List<WorkoutStat> getWorkoutStats();

    Completion getCompletion();
}
