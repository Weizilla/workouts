package com.weizilla.workouts.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value.Immutable;

import javax.measure.Quantity;
import javax.measure.quantity.Length;
import java.time.Duration;
import java.time.LocalDate;

@Immutable
@JsonSerialize(as = ImmutableCreateRecordDto.class)
@JsonDeserialize(as = ImmutableCreateRecordDto.class)
public interface CreateRecordDto {
    String getType();
    LocalDate getDate();
    int getRating();
    Quantity<Length> getDistance();
    Duration getDuration();
    String getComment();
}
