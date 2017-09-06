package com.weizilla.workouts.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.weizilla.distance.Distance;
import org.immutables.value.Value.Derived;
import org.immutables.value.Value.Immutable;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

//TODO come up with a better name that doesn't collide with one from Garmin lib
@Immutable
@JsonSerialize(as = ImmutableActivity.class)
@JsonDeserialize(as = ImmutableActivity.class)
@WorkoutsStyle
public interface Activity extends Entry<Long> {

    LocalDateTime getStart();

    @Derived
    @Override
    default LocalDate getDate() {
        return getStart().toLocalDate();
    }

    Duration getDuration();

    Distance getDistance();
}
