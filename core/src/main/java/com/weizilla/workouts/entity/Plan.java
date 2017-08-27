package com.weizilla.workouts.entity;

import org.immutables.value.Value.Immutable;

import javax.measure.Quantity;
import javax.measure.quantity.Length;
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
    Quantity<Length> getDistance();
    Duration getDuration();
    String getNotes();
}
