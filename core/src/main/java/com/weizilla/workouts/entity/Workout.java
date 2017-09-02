package com.weizilla.workouts.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value.Default;

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

    UUID getRecordId();
    LocalDateTime getStartTime();
    int getRating();
    List<Long> getGarminIds();
    String getComment();
}
