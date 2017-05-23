package com.weizilla.workouts.entity;

import org.immutables.value.Value.Immutable;

import javax.annotation.Nullable;
import javax.measure.Quantity;
import javax.measure.quantity.Length;
import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;


@Immutable
public interface Record {
    UUID getId();
    String getType();
    LocalDate getDate();
    Integer getRating();
    String getComment();
    @Nullable Duration getDuration();
    @Nullable Quantity<Length> getDistance();
}
