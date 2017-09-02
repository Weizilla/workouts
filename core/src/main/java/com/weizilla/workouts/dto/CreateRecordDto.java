package com.weizilla.workouts.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.weizilla.distance.Distance;
import com.weizilla.workouts.entity.WorkoutsStyle;
import org.immutables.value.Value.Immutable;

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
    Distance getDistance();
    Duration getDuration();
    String getComment();
}
