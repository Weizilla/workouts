package com.weizilla.workouts.entity;

import javax.measure.Quantity;
import javax.measure.quantity.Length;
import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

public class Plan {
    private final UUID id;
    private final String type;
    private final LocalDate date;
    private final TimeOfDay timeOfDay;
    private Quantity<Length> distance;
    private Duration duration;
    private String notes;

    public Plan(UUID id, String type, LocalDate date, TimeOfDay timeOfDay) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.timeOfDay = timeOfDay;
    }

    public void setDistance(Quantity<Length> distance) {
        this.distance = distance;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public Quantity<Length> getDistance() {
        return distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getNotes() {
        return notes;
    }

    public TimeOfDay getTimeOfDay() {
        return timeOfDay;
    }
}
