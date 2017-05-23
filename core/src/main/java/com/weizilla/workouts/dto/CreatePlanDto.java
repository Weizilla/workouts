package com.weizilla.workouts.dto;

import com.weizilla.workouts.entity.TimeOfDay;

import javax.measure.Quantity;
import javax.measure.quantity.Length;
import java.time.Duration;
import java.time.LocalDate;

public class CreatePlanDto {
    private TimeOfDay timeOfDay;
    private String type;
    private LocalDate date;
    private Duration duration;
    private Quantity<Length> distance;
    private String notes;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Quantity<Length> getDistance() {
        return distance;
    }

    public void setDistance(Quantity<Length> distance) {
        this.distance = distance;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public TimeOfDay getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(TimeOfDay timeOfDay) {
        this.timeOfDay = timeOfDay;
    }
}
