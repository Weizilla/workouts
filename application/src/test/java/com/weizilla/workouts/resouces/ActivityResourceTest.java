package com.weizilla.workouts.resouces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.weizilla.garmin.entity.Activity;
import com.weizilla.garmin.entity.ImmutableActivity;
import com.weizilla.workouts.entity.ObjectMappers;
import com.weizilla.workouts.interactor.GetActivities;
import com.weizilla.workouts.interactor.UpdateGarminStore;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import tec.uom.se.quantity.Quantities;

import javax.measure.Quantity;
import javax.measure.quantity.Length;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static systems.uom.common.USCustomary.MILE;

@RunWith(MockitoJUnitRunner.class)
public class ActivityResourceTest {
    protected static final long ID = 100;
    protected static final String TYPE = "TYPE";
    protected static final LocalDateTime START = LocalDateTime.now();
    protected static final Duration DURATION = Duration.ofHours(1);
    protected static final Quantity<Length> DISTANCE = Quantities.getQuantity(BigDecimal.valueOf(1.0), MILE);
    private static final Activity ACTIVITY = ImmutableActivity.builder()
        .id(ID)
        .type(TYPE)
        .start(START)
        .duration(DURATION)
        .distance(DISTANCE)
        .build();

    private static final GetActivities getActivities = mock(GetActivities.class);
    private static final UpdateGarminStore updateGarminStore = mock(UpdateGarminStore.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
        .addResource(new ActivityResource(getActivities, updateGarminStore))
        .setMapper(ObjectMappers.OBJECT_MAPPER)
        .build();
    private static final TypeReference<List<Activity>> TYPE_REF = new TypeReference<List<Activity>>() {
    };

    @After
    public void tearDown() throws Exception {
        reset(getActivities, updateGarminStore);
    }

    @Test
    public void getsAllActivities() throws Exception {
        when(getActivities.getAll()).thenReturn(singletonList(ACTIVITY));
        String jsonResult = resources.target("/activities").request().get(String.class);
        List<Activity> results = ObjectMappers.OBJECT_MAPPER.readValue(jsonResult, TYPE_REF);
        assertThat(results).containsExactly(ACTIVITY);
    }

    @Test
    public void getsActivitiesByDate() throws Exception {
        LocalDate date = START.toLocalDate();
        when(getActivities.get(date)).thenReturn(singletonList(ACTIVITY));

        String jsonResult = resources.target("/activities")
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