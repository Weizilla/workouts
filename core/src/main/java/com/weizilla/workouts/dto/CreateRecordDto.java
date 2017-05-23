package com.weizilla.workouts.dto;

import org.immutables.value.Value.Immutable;

import javax.measure.Quantity;
import javax.measure.quantity.Length;
import java.time.Duration;
import java.time.LocalDate;

@Immutable
public interface CreateRecordDto {
    String getType();
    LocalDate getDate();
    int getRating();
    Quantity<Length> getDistance();
    Duration getDuration();
    String getComment();
}
