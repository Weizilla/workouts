package com.weizilla.workouts.entity;

import com.weizilla.distance.Distance;
import org.immutables.value.Value.Immutable;

import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

@Immutable
@WorkoutsStyle
public interface Plan {
    UUID getId();
    String getType();
    LocalDate getDate();
    TimeOfDay getTimeOfDay();
    Distance getDistance();
    Duration getDuration();
    String getNotes();
}
