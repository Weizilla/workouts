package com.weizilla.workouts.interactor;

import com.weizilla.garmin.entity.Activity;
import com.weizilla.workouts.entity.ImmutableWorkout;
import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.entity.Workout;
import com.weizilla.workouts.store.GarminStore;
import com.weizilla.workouts.store.RecordStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.measure.Quantity;
import javax.measure.quantity.Length;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class GetWorkouts {
    private static final Logger logger = LoggerFactory.getLogger(GetWorkouts.class);
    private final RecordStore recordStore;
    private final GarminStore garminStore;

    public GetWorkouts(RecordStore recordStore, GarminStore garminStore) {
        this.recordStore = recordStore;
        this.garminStore = garminStore;
    }

    public List<Workout> getWorkouts(LocalDate date) {
        Map<String, List<Record>> records = recordStore.get(date).stream()
            .collect(Collectors.groupingBy(Record::getType, Collectors.toList()));
        Map<String, List<Activity>> activities = garminStore.get(date).stream()
            .collect(Collectors.groupingBy(Activity::getType, Collectors.toList()));

        List<Workout> workouts = records.entrySet().stream()
            .map(e -> create(e.getValue(), activities.get(e.getKey())))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
        return workouts;
    }

    private static Optional<Workout> create(List<Record> records, List<Activity> activities) {
        if (activities == null || activities.isEmpty()) {
            return Optional.empty();
        }

        Record record = records.get(0);
        if (records.size() > 1) {
            logger.warn("Can only match one record per type for {}", record.getDate());
        }

        Duration duration = record.getDuration() != null
            ? record.getDuration()
            : activities.stream()
                .map(Activity::getDuration)
                .reduce(Duration::plus).get();

        Quantity<Length> distance = record.getDistance() != null
            ? record.getDistance()
            : activities.stream()
                .map(Activity::getDistance)
                .reduce(Quantity::add).get();



        List<Long> garminIds = activities.stream()
            .map(Activity::getId)
            .collect(Collectors.toList());

        Workout workout = ImmutableWorkout.builder()
            .recordId(record.getId())
            .type(record.getType())
            .startTime(activities.get(0).getStart())
            .rating(record.getRating())
            .duration(duration)
            .distance(distance)
            .garminIds(garminIds)
            .comment(record.getComment())
            .build();

        return Optional.of(workout);
    }
}
