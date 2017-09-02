package com.weizilla.workouts.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import javax.annotation.Nullable;
import java.util.UUID;

@Immutable
@JsonSerialize(as = ImmutableRecord.class)
@JsonDeserialize(as = ImmutableRecord.class)
@WorkoutsStyle
public interface Record extends Entry<UUID> {
    @Override
    @Default
    default UUID getId() {
        return UUID.randomUUID();
    }

    boolean isOutdoor();

    Integer getRating();

    @Nullable
    String getComment();
}
