package com.weizilla.workouts.entity;

import javax.measure.Quantity;
import javax.measure.quantity.Length;
import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

public class Record {
    private final UUID id;
    private final String type;
    private final LocalDate date;
    private final int rating;
    private Quantity<Length> distance;
    private Duration duration;
    private String comment;

    public Record(UUID id, String type, LocalDate date, Integer rating) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.rating = rating;
    }

    public UUID getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }

    public Duration getDuration() {
        return duration;
    }

    public Integer getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public Quantity<Length> getDistance() {
        return distance;
    }

    public void setDistance(Quantity<Length> distance) {
        this.distance = distance;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
