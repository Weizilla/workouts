package com.weizilla.workouts.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value.Immutable;

import java.time.Instant;
import java.util.List;

@Immutable
@JsonSerialize(as = ImmutableExport.class)
@JsonDeserialize(as = ImmutableExport.class)
@WorkoutsStyle
public interface Export {
    List<Activity> getActivities();
    List<Record> getRecords();
    List<Goal> getGoals();
    Instant getGenerated();
}
