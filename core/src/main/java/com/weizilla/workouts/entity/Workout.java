package com.weizilla.workouts.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.weizilla.distance.Distance;
import org.immutables.value.Value.Immutable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Immutable
@JsonSerialize(as = ImmutableWorkout.class)
@JsonDeserialize(as = ImmutableWorkout.class)
@WorkoutsStyle
public interface Workout {
    UUID getRecordId();
    String getType();
    LocalDateTime getStartTime();
    int getRating();
    Duration getDuration();
    Distance getDistance();
    List<Long> getGarminIds();
    String getComment();
}
