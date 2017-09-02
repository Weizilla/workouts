package com.weizilla.workouts.entity;

import com.weizilla.distance.Distance;
import org.immutables.value.Value.Default;

import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;

public interface Entry<T> {

    T getId();

    String getType();

    LocalDate getDate();

    @Default
    default Instant getCreatedTime() {
        return Instant.now();
    }

    //TODO use optional
    @Nullable
    Duration getDuration();

    //TODO use optional
    @Nullable
    Distance getDistance();
}
