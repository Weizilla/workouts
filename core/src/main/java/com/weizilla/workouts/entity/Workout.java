package com.weizilla.workouts.entity;

import javax.measure.Quantity;
import javax.measure.quantity.Length;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Workout {
    private final UUID recordId;
    private final String type;
    private final LocalDateTime startTime;
    private final int rating;
    private final Duration duration;
    private final Quantity<Length> distance;
    private final List<Long> garminIds;
    private final String comment;

    public Workout(UUID recordId, String type, LocalDateTime startTime, int rating,
        Duration duration, Quantity<Length> distance, List<Long> garminIds, String comment) {
        this.recordId = recordId;
        this.type = type;
        this.startTime = startTime;
        this.rating = rating;
        this.duration = duration;
        this.distance = distance;
        this.garminIds = garminIds;
        this.comment = comment;
    }

    public UUID getRecordId() {
        return recordId;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public int getRating() {
        return rating;
    }

    public Duration getDuration() {
        return duration;
    }

    public Quantity<Length> getDistance() {
        return distance;
    }

    public List<Long> getGarminIds() {
        return garminIds;
    }

    public String getComment() {
        return comment;
    }
}
