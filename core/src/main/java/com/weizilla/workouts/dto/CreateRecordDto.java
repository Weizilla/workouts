package com.weizilla.workouts.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.weizilla.workouts.entity.WorkoutsStyle;
import org.immutables.value.Value.Immutable;

import javax.measure.Quantity;
import javax.measure.quantity.Length;
import java.time.Duration;
import java.time.LocalDate;

@Immutable
@JsonSerialize(as = ImmutableCreateRecordDto.class)
@JsonDeserialize(as = ImmutableCreateRecordDto.class)
@WorkoutsStyle
public interface CreateRecordDto {
    String getType();
    boolean isOutdoor();
    LocalDate getDate();
    int getRating();
    Quantity<Length> getDistance();
    Duration getDuration();
    String getComment();
}
