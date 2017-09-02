package com.weizilla.workouts.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import java.time.LocalDate;
import java.time.LocalDateTime;

//TODO come up with a better name that doesn't collide with one from Garmin lib
@Immutable
@JsonSerialize(as = ImmutableActivity.class)
@JsonDeserialize(as = ImmutableActivity.class)
@WorkoutsStyle
public interface Activity extends Entry<Long> {
    LocalDateTime getStart();

    @Default
    @Override
    default LocalDate getDate() {
        return getStart().toLocalDate();
    }
}
