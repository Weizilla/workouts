package com.weizilla.workouts.interactor;

import com.weizilla.garmin.downloader.ActivityDownloader;
import com.weizilla.garmin.entity.Activity;
import com.weizilla.workouts.store.GarminStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import systems.uom.common.USCustomary;
import tec.uom.se.quantity.Quantities;

import javax.measure.Quantity;
import javax.measure.quantity.Length;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateGarminStoreTest {
    private UpdateGarminStore updateGarminStore;
    @Mock
    private ActivityDownloader activityDownloader;
    @Mock
    private GarminStore garminStore;
    private List<Activity> activities;

    @Before
    public void setUp() throws Exception {
        long id = 100L;
        String type = "TYPE";
        Duration duration = Duration.ofHours(1);
        LocalDateTime start = LocalDateTime.now();
        Quantity<Length> distance = Quantities.getQuantity(10.0, USCustomary.MILE);
        Activity activity = new Activity(id, type, duration, start, distance);
        activities = Collections.singletonList(activity);
        when(activityDownloader.download()).thenReturn(activities);

        updateGarminStore = new UpdateGarminStore(activityDownloader, garminStore);
    }

    @Test
    public void updateShouldGetActivitiesFromSourceAndAddToStore() throws Exception {
        int actual = updateGarminStore.update();
        verify(garminStore).addAll(activities);
        assertThat(actual).isEqualTo(activities.size());
    }
}