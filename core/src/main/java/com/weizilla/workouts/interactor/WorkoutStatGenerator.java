package com.weizilla.workouts.interactor;

import com.google.common.collect.Sets;
import com.weizilla.distance.Distance;
import com.weizilla.workouts.entity.Activity;
import com.weizilla.workouts.entity.Completion;
import com.weizilla.workouts.entity.DayStat;
import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.entity.ImmutableDayStat;
import com.weizilla.workouts.entity.ImmutableWorkoutStat;
import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.entity.WorkoutStat;
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
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

//TODO probably needs to be split into multiple classes
@Singleton
public class WorkoutStatGenerator {
    private static final Logger logger = LoggerFactory.getLogger(WorkoutStatGenerator.class);
    private final RecordStore recordStore;
    private final GarminStore garminStore;
    private final GoalStore goalStore;
    private final CompletionCalculator completionCalculator;

    @Inject
    public WorkoutStatGenerator(RecordStore recordStore, GarminStore garminStore, GoalStore goalStore,
            CompletionCalculator completionCalculator) {
        this.recordStore = recordStore;
        this.garminStore = garminStore;
        this.goalStore = goalStore;
        this.completionCalculator = completionCalculator;
    }

    public List<DayStat> generateAll() {
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
            .map(this::generate)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }

    public Optional<DayStat> generate(LocalDate date) {
        Map<String, List<Record>> records = recordStore.get(date).stream()
            .collect(Collectors.groupingBy(Record::getType, Collectors.toList()));
        Map<String, List<Activity>> activities = garminStore.get(date).stream()
            .collect(Collectors.groupingBy(Activity::getType, Collectors.toList()));
        Map<String, List<Goal>> goals = goalStore.get(date).stream()
            .collect(Collectors.groupingBy(Goal::getType, Collectors.toList()));

        Set<String> allTypes = Sets.union(Sets.union(records.keySet(), activities.keySet()), goals.keySet());

        List<WorkoutStat> workouts = allTypes.stream()
            .map(t -> generate(t, date,
                records.getOrDefault(t, Collections.emptyList()),
                activities.getOrDefault(t, Collections.emptyList()),
                goals.getOrDefault(t, Collections.emptyList())))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());

        if (! workouts.isEmpty()) {
            Completion completion = calcCompletion(workouts);
            DayStat dayStat = ImmutableDayStat.builder()
                .addAllWorkoutStats(workouts)
                .completion(completion)
                .date(date)
                .build();
            return Optional.of(dayStat);
        } else {
            return Optional.empty();
        }
    }

    private Optional<WorkoutStat> generate(String type, LocalDate date,
        List<Record> records, List<Activity> activities, List<Goal> goals) {

        if (records.isEmpty() && activities.isEmpty() && goals.isEmpty()) {
            return Optional.empty();
        }

        //TODO better handling. Either all goals or just one goal
        Optional<Goal> goal = goals.isEmpty() ? Optional.empty() : Optional.of(goals.get(0));
        if (goals.size() > 1) {
            logger.warn("Can only match one goal per type for {}", date);
        }

        Optional<Duration> totalDuration = sumDuration(records, Record::getDuration)
            .map(Optional::of)
            .orElse(sumDuration(activities, a -> Optional.of(a.getDuration())));
        Optional<Distance> totalDistance = sumDistance(records, Record::getDistance)
            .map(Optional::of)
            .orElse(sumDistance(activities, a -> Optional.of(a.getDistance())));

        Optional<Duration> goalDuration = sumDuration(goals, Goal::getDuration);
        Optional<Distance> goalDistance = sumDistance(goals, Goal::getDistance);

        Completion completion;
        if (date.isAfter(LocalDate.now())) {
            completion = Completion.GOAL;
        } else if (goal.isPresent() && records.isEmpty() && activities.isEmpty()) {
            completion = Completion.NONE;
        } else {
            completion = completionCalculator.calculate(goalDuration, goalDistance, totalDuration, totalDistance);
        }

        WorkoutStat workout = ImmutableWorkoutStat.builder()
            .records(records)
            .type(type)
            .date(date)
            .goal(goal)
            .completion(completion)
            .totalDuration(totalDuration)
            .totalDistance(totalDistance)
            .goalDuration(goalDuration)
            .goalDistance(goalDistance)
            .addAllActivities(activities)
            .build();

        return Optional.of(workout);
    }

    private static <T> Optional<Duration> sumDuration(Collection<T> collection, Function<T, Optional<Duration>> mapper) {
        return sum(collection, mapper, Duration::plus);
    }

    private static <T> Optional<Distance> sumDistance(Collection<T> collection, Function<T, Optional<Distance>> mapper) {
        return sum(collection, mapper, Distance::plus);
    }

    private static <T, R> Optional<R> sum(Collection<T> collection, Function<T, Optional<R>> extractFunction,
            BinaryOperator<R> accumulator) {
        if (collection == null || collection.isEmpty()) {
            return Optional.empty();
        }

        return collection.stream()
            .map(extractFunction)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .reduce(accumulator);
    }

    private static Completion calcCompletion(List<WorkoutStat> workouts) {
        Set<Completion> all = workouts.stream()
            .map(WorkoutStat::getCompletion)
            .collect(Collectors.toSet());

        return all.size() == 1 ? workouts.get(0).getCompletion() : Completion.SOME;
    }
}
