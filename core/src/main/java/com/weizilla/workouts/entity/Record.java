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
@JsonSerialize(as = ImmutableRecord.class)
@JsonDeserialize(as = ImmutableRecord.class)
@WorkoutsStyle
public interface Record extends Entry<UUID> {

    @Default
    @Override
    default UUID getId() {
        return UUID.randomUUID();
    }

    boolean isOutdoor();

    int getRating();

    Optional<Duration> getDuration();

    Optional<Distance> getDistance();

    String getComment();
}
