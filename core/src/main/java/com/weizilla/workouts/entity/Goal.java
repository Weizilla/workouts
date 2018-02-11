package com.weizilla.workouts.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.weizilla.distance.Distance;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Immutable
@JsonSerialize(as = ImmutableGoal.class)
@JsonDeserialize(as = ImmutableGoal.class)
@WorkoutsStyle
public interface Goal extends Entry<UUID> {

    @Default
    @Override
    default UUID getId() {
        return UUID.randomUUID();
    }

    TimeOfDay getTimeOfDay();

    Optional<Duration> getDuration();

    Optional<Distance> getDistance();

    String getNotes();
}
