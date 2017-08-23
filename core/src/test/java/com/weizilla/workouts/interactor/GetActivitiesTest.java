package com.weizilla.workouts.interactor;

import com.weizilla.garmin.entity.Activity;
import com.weizilla.garmin.entity.ImmutableActivity;
import com.weizilla.workouts.store.GarminStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static systems.uom.common.USCustomary.MILE;
import static tec.uom.se.quantity.Quantities.getQuantity;

@RunWith(MockitoJUnitRunner.class)
public class GetActivitiesTest {
    private static final LocalDate DATE = LocalDate.now();
    @Mock
    private GarminStore garminStore;
    private GetActivities getActivities;
    private Activity activity;

    @Before
    public void setUp() throws Exception {
        getActivities = new GetActivities(garminStore);
        activity = ImmutableActivity.builder()
            .id(1)
            .type("TYPE")
            .start(now())
            .duration(Duration.ofHours(1))
            .distance(getQuantity(1, MILE))
            .build();
    }

    @Test
    public void getShouldReturnActivityByDate() throws Exception {
        when(garminStore.get(DATE)).thenReturn(Collections.singletonList(activity));
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
        when(garminStore.getAll()).thenReturn(Collections.singletonList(activity));
        List<Activity> actual = getActivities.getAll();
        assertThat(actual).containsExactly(activity);
    }
}