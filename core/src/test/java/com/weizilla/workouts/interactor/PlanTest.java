package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.ImmutablePlan;
import com.weizilla.workouts.entity.Plan;
import com.weizilla.workouts.entity.TimeOfDay;
import com.weizilla.workouts.store.PlanStore;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import tec.uom.se.quantity.Quantities;

import javax.measure.Quantity;
import javax.measure.quantity.Length;
import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

import static systems.uom.common.USCustomary.MILE;

@RunWith(MockitoJUnitRunner.class)
public abstract class PlanTest {
    protected static final UUID ID = UUID.randomUUID();
    protected static final String TYPE = "TYPE";
    protected static final LocalDate DATE = LocalDate.now();
    protected static final TimeOfDay TIME_OF_DAY = TimeOfDay.MORNING;
    protected static final Duration DURATION = Duration.ofHours(1);
    protected static final Quantity<Length> DISTANCE = Quantities.getQuantity(1.0, MILE);
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
