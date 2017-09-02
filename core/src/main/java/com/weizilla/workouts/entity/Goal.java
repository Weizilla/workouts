package com.weizilla.workouts.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import java.util.UUID;

@Immutable
@JsonSerialize(as = ImmutableGoal.class)
@JsonDeserialize(as = ImmutableGoal.class)
@WorkoutsStyle
public interface Goal extends Entry<UUID> {
    @Override
    @Default
    default UUID getId() {
        return UUID.randomUUID();
    }

    TimeOfDay getTimeOfDay();
    String getNotes();
}