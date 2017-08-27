package com.weizilla.workouts.dto;

import com.weizilla.workouts.entity.TimeOfDay;
import com.weizilla.workouts.entity.WorkoutsStyle;
import org.immutables.value.Value.Immutable;

import javax.measure.Quantity;
import javax.measure.quantity.Length;
import java.time.Duration;
import java.time.LocalDate;

@Immutable
@WorkoutsStyle
public interface CreatePlanDto {
    String getType();
    LocalDate getDate();
    Duration getDuration();
    Quantity<Length> getDistance();
    String getNotes();
    TimeOfDay getTimeOfDay();
}
