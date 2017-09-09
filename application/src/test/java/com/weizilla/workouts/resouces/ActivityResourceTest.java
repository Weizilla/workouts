package com.weizilla.workouts.resouces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.weizilla.workouts.entity.Activity;
import com.weizilla.workouts.entity.ObjectMappers;
import com.weizilla.workouts.entity.TestEntity;
import com.weizilla.workouts.interactor.GetActivities;
import com.weizilla.workouts.interactor.UpdateGarminStore;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.List;

import static com.weizilla.workouts.entity.TestEntity.START;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ActivityResourceTest {
    private static final Activity ACTIVITY = TestEntity.createActivity();
    private static final GetActivities GET_ACTIVITIES = mock(GetActivities.class);
    private static final UpdateGarminStore UPDATE_GARMIN_STORE = mock(UpdateGarminStore.class);
    private static final TypeReference<List<Activity>> TYPE_REF = new TypeReference<List<Activity>>() { };

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
        .addResource(new ActivityResource(GET_ACTIVITIES, UPDATE_GARMIN_STORE))
        .setMapper(ObjectMappers.OBJECT_MAPPER)
        .build();

    @After
    public void tearDown() throws Exception {
        reset(GET_ACTIVITIES, UPDATE_GARMIN_STORE);
    }

    @Test
    public void getsAllActivities() throws Exception {
        when(GET_ACTIVITIES.getAll()).thenReturn(singletonList(ACTIVITY));
        String jsonResult = RESOURCES.target("/activities").request().get(String.class);
        List<Activity> results = ObjectMappers.OBJECT_MAPPER.readValue(jsonResult, TYPE_REF);
        assertThat(results).containsExactly(ACTIVITY);
    }

    @Test
    public void getsActivitiesByDate() throws Exception {
        LocalDate date = START.toLocalDate();
        when(GET_ACTIVITIES.get(date)).thenReturn(singletonList(ACTIVITY));

        String jsonResult = RESOURCES.target("/activities")
            .queryParam("date", date).request().get(String.class);
        List<Activity> results = ObjectMappers.OBJECT_MAPPER.readValue(jsonResult, TYPE_REF);
        assertThat(results).containsExactly(ACTIVITY);
    }

    //TODO job tracking
//    @Test
//    public void updatesGarminStore() throws Exception {
//        int expected = 10;
//        when(updateGarminStore.startUpdate()).thenReturn(expected);
//
//        String jsonResult = resources.target("/activities/update").request().get(String.class);
//        JsonNode results = ObjectMappers.OBJECT_MAPPER.readTree(jsonResult);
//        assertThat(results.path("downloaded").asInt()).isEqualTo(expected);
//    }
}