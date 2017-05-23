package com.weizilla.workouts.dto;

import javax.measure.Quantity;
import javax.measure.quantity.Length;
import java.time.Duration;
import java.time.LocalDate;

public class CreateRecordDto {
    private String type;
    private LocalDate date;
    private Duration duration;
    private Quantity<Length> distance;
    private Integer rating;
    private String comment;

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

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Quantity<Length> getDistance() {
        return distance;
    }

    public void setDistance(Quantity<Length> distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getComment() {
        return comment;
    }
}
