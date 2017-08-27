package com.weizilla.workouts.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value.Immutable;

import javax.annotation.Nullable;
import javax.measure.Quantity;
import javax.measure.quantity.Length;
import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

@Immutable
@JsonSerialize(as = ImmutableRecord.class)
@JsonDeserialize(as = ImmutableRecord.class)
@WorkoutsStyle
public interface Record {
    UUID getId();
    String getType();
    boolean isOutdoor();
    LocalDate getDate();
    Integer getRating();
    String getComment();
    @Nullable Duration getDuration();
    @Nullable Quantity<Length> getDistance();
}
