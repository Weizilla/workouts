package com.weizilla.workouts.dto;

import com.google.common.base.Strings;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Optional;

public class CreateDto {
    private String type;
    private LocalDate date;
    private Duration duration;
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

    public Optional<Duration> getDuration() {
        return Optional.ofNullable(duration);
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

    public Optional<String> getComment() {
        return Optional.ofNullable(Strings.emptyToNull(comment));
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
