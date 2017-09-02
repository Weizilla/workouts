package com.weizilla.workouts.interactor;

import com.weizilla.distance.Distance;
import com.weizilla.workouts.entity.ImmutablePlan;
import com.weizilla.workouts.entity.Plan;
import com.weizilla.workouts.entity.TimeOfDay;
import com.weizilla.workouts.store.PlanStore;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public abstract class PlanTest {
    protected static final UUID ID = UUID.randomUUID();
    protected static final String TYPE = "TYPE";
    protected static final LocalDate DATE = LocalDate.now();
    protected static final TimeOfDay TIME_OF_DAY = TimeOfDay.MORNING;
    protected static final Duration DURATION = Duration.ofHours(1);
    protected static final Distance DISTANCE = Distance.ofMiles(1);
    protected static final String NOTES = "NOTES";
    protected Plan plan;
    @Mock
    protected PlanStore planStore;

    @Before
    public void setUp() throws Exception {
        plan = ImmutablePlan.builder()
            .id(ID)
            .type(TYPE)
            .date(DATE)
            .timeOfDay(TIME_OF_DAY)
            .duration(DURATION)
            .distance(DISTANCE)
            .notes(NOTES)
            .build();
    }
}
