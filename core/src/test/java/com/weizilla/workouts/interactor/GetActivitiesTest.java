package com.weizilla.workouts.interactor;

import com.weizilla.distance.Distance;
import com.weizilla.workouts.entity.Activity;
import com.weizilla.workouts.entity.ImmutableActivity;
import com.weizilla.workouts.store.GarminStore;
import com.weizilla.workouts.store.MemoryGarminStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class GetActivitiesTest {
    private static final LocalDate DATE = LocalDate.now();
    private GarminStore garminStore;
    private GetActivities getActivities;
    private Activity activity;

    @Before
    public void setUp() throws Exception {
        garminStore = new MemoryGarminStore();
        getActivities = new GetActivities(garminStore);
        activity = ImmutableActivity.builder()
            .id(1L)
            .type("TYPE")
            .start(now())
            .duration(Duration.ofHours(1))
            .distance(Distance.ofMiles(1))
            .build();
    }

    @Test
    public void getShouldReturnActivityByDate() throws Exception {
        garminStore.add(activity);
        List<Activity> actual = getActivities.get(DATE);
        assertThat(actual).containsExactly(activity);
    }

    @Test
    public void getShouldReturnEmptyListIfNoActivitysFound() throws Exception {
        List<Activity> actual = getActivities.get(DATE);
        assertThat(actual).isEmpty();
    }

    @Test
    public void getReturnAllActivities() throws Exception {
        garminStore.add(activity);
        List<Activity> actual = getActivities.getAll();
        assertThat(actual).containsExactly(activity);
    }
}