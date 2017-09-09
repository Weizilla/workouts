package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.Activity;
import com.weizilla.workouts.entity.TestEntity;
import com.weizilla.workouts.store.GarminStore;
import com.weizilla.workouts.store.MemoryGarminStore;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.weizilla.workouts.entity.TestEntity.DATE;
import static org.assertj.core.api.Assertions.assertThat;

public class GetActivitiesTest {
    private GarminStore garminStore;
    private GetActivities getActivities;
    private Activity activity;

    @Before
    public void setUp() throws Exception {
        garminStore = new MemoryGarminStore();
        getActivities = new GetActivities(garminStore);
        activity = TestEntity.createActivity();
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