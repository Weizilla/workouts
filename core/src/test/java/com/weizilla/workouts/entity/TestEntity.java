package com.weizilla.workouts.entity;

import com.weizilla.distance.Distance;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

public class TestEntity {
    public static final UUID RECORD_ID = UUID.randomUUID();
    public static final UUID WORKOUT_ID = UUID.randomUUID();
    public static final UUID GOAL_ID = UUID.randomUUID();
    public static final long ACTIVITY_ID = new Random().nextInt(1000);
    public static final String TYPE = "TYPE";
    public static final boolean OUTDOOR = true;
    public static final LocalDateTime START = LocalDateTime.now();
    public static final LocalDate DATE = LocalDate.now();
    public static final int RATING = 3;
    public static final Duration DURATION = Duration.ofHours(7);
    public static final Distance DISTANCE = Distance.ofMiles(70.3);
    public static final Duration GOAL_DURATION = Duration.ofHours(14);
    public static final Distance GOAL_DISTANCE = Distance.ofMiles(140.6);
    public static final String COMMENT = "COMMENT";
    public static final String NOTES = "NOTES";
    public static final TimeOfDay TIME_OF_DAY = TimeOfDay.MORNING;

    public static Record createRecord() {
        return ImmutableRecord.builder()
            .id(RECORD_ID)
            .type(TYPE)
            .outdoor(OUTDOOR)
            .date(DATE)
            .rating(RATING)
            .duration(DURATION)
            .distance(DISTANCE)
            .comment(COMMENT)
            .build();
    }

    public static Activity createActivity() {
        return ImmutableActivity.builder()
            .id(ACTIVITY_ID)
            .type(TYPE)
            .startTime(START)
            .duration(DURATION)
            .distance(DISTANCE)
            .build();
    }

    public static Workout createWorkout() {
        return ImmutableWorkout.builder()
            .id(WORKOUT_ID)
            .type(TYPE)
            .date(DATE)
            .totalDistance(DISTANCE)
            .totalDuration(DURATION)
            .goalDistance(GOAL_DISTANCE)
            .goalDuration(GOAL_DURATION)
            .addActivities(createActivity())
            .record(createRecord())
            .goal(createGoal())
            .build();
    }

    public static Goal createGoal() {
        return ImmutableGoal.builder()
            .id(GOAL_ID)
            .type(TYPE)
            .date(DATE)
            .notes(NOTES)
            .timeOfDay(TIME_OF_DAY)
            .distance(GOAL_DISTANCE)
            .duration(GOAL_DURATION)
            .build();
    }
}
