package com.weizilla.workouts.interactor;

import com.weizilla.distance.Distance;
import com.weizilla.garmin.downloader.ActivityDownloader;
import com.weizilla.garmin.entity.Activity;
import com.weizilla.garmin.entity.ImmutableActivity;
import com.weizilla.workouts.store.GarminStore;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UpdateGarminStoreTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private ActivityDownloader activityDownloader;
    @Mock
    private GarminStore garminStore;
    private UpdateGarminStore updateGarminStore;
    private List<Activity> activities;

    @Before
    public void setUp() throws Exception {
        long id = 100L;
        String type = "TYPE";
        Duration duration = Duration.ofHours(1);
        LocalDateTime start = LocalDateTime.now();
        Distance distance = Distance.ofMiles(10);
        Activity activity = ImmutableActivity.builder()
            .id(id)
            .type(type)
            .start(start)
            .duration(duration)
            .distance(distance)
            .build();
        activities = Collections.singletonList(activity);
        when(activityDownloader.download()).thenReturn(activities);

        updateGarminStore = new UpdateGarminStore(activityDownloader, garminStore);
    }

    @Test
    public void updateShouldGetActivitiesFromSourceAndAddToStore() throws Exception {
        Future<?> job = updateGarminStore.startUpdate();
        job.get();
        verify(garminStore).addAll(activities);
    }
}