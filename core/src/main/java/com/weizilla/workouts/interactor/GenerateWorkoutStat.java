package com.weizilla.workouts.interactor;

import com.google.common.collect.Sets;
import com.weizilla.distance.Distance;
import com.weizilla.workouts.entity.Activity;
import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.entity.ImmutableWorkout;
import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.entity.Workout;
import com.weizilla.workouts.store.GarminStore;
import com.weizilla.workouts.store.GoalStore;
import com.weizilla.workouts.store.RecordStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Singleton
public class GenerateWorkoutStat {
    private static final Logger logger = LoggerFactory.getLogger(GenerateWorkoutStat.class);
    private final RecordStore recordStore;
    private final GarminStore garminStore;
    private final GoalStore goalStore;

    @Inject
    public GenerateWorkoutStat(RecordStore recordStore, GarminStore garminStore, GoalStore goalStore) {
        this.recordStore = recordStore;
        this.garminStore = garminStore;
        this.goalStore = goalStore;
    }

    public List<Workout> getAll() {
        Set<LocalDate> allDates = new TreeSet<>();
        recordStore.getAll().stream()
            .map(Record::getDate)
            .forEach(allDates::add);
        garminStore.getAll().stream()
            .map(Activity::getDate)
            .forEach(allDates::add);
        goalStore.getAll().stream()
            .map(Goal::getDate)
            .forEach(allDates::add);

        return allDates.stream()
            .flatMap(d -> get(d).stream())
            .collect(Collectors.toList());
    }

    public List<Workout> get(LocalDate date) {
        Map<String, List<Record>> records = recordStore.get(date).stream()
            .collect(Collectors.groupingBy(Record::getType, Collectors.toList()));
        Map<String, List<Activity>> activities = garminStore.get(date).stream()
            .collect(Collectors.groupingBy(Activity::getType, Collectors.toList()));
        Map<String, List<Goal>> goals = goalStore.get(date).stream()
            .collect(Collectors.groupingBy(Goal::getType, Collectors.toList()));

        Set<String> allTypes = Sets.union(Sets.union(records.keySet(), activities.keySet()), goals.keySet());

        List<Workout> workouts = allTypes.stream()
            .map(t -> create(t, date,
                records.getOrDefault(t, Collections.emptyList()),
                activities.getOrDefault(t, Collections.emptyList()),
                goals.getOrDefault(t, Collections.emptyList())))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
        return workouts;
    }

    private static Optional<Workout> create(String type, LocalDate date,
            List<Record> records, List<Activity> activities, List<Goal> goals) {

        if (records.isEmpty() && activities.isEmpty() && goals.isEmpty()) {
            return Optional.empty();
        }

        Record record = records.isEmpty() ? null : records.get(0);
        if (records.size() > 1) {
            logger.warn("Can only match one record per type for {}", date);
        }

        Goal goal = goals.isEmpty() ? null : goals.get(0);
        if (goals.size() > 1) {
            logger.warn("Can only match one goal per type for {}", date);
        }

        Duration totalDuration = record != null && record.getDuration() != null
            ? record.getDuration()
            : activities.stream()
                .map(Activity::getDuration)
                .reduce(Duration::plus).orElse(null);

        Distance totalDistance = record != null && record.getDistance() != null
            ? record.getDistance()
            : activities.stream()
                .map(Activity::getDistance)
                .reduce(Distance::plus).orElse(null);

        Workout workout = ImmutableWorkout.builder()
            .record(record)
            .type(type)
            .date(date)
            .goal(goal)
            .totalDuration(totalDuration)
            .totalDistance(totalDistance)
            .addAllActivities(activities)
            .build();

        return Optional.of(workout);
    }

    private static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
