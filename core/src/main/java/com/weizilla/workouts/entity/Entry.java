package com.weizilla.workouts.entity;

import org.immutables.value.Value.Default;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoField;

public interface Entry<K> extends Comparable<Entry<K>> {

    K getId();

    String getType();

    LocalDate getDate();

    @Default
    default Instant getCreatedTime() {
        return Instant.now().with(ChronoField.NANO_OF_SECOND, 0);
    }

    @Override
    default int compareTo(Entry<K> o) {
        int result = getDate().compareTo(o.getDate());
        if (result == 0) {
            result = getCreatedTime().compareTo(o.getCreatedTime());
        }
        return result;
    }
}
