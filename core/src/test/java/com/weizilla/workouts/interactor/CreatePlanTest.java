package com.weizilla.workouts.interactor;

import com.weizilla.workouts.dto.CreatePlanDto;
import com.weizilla.workouts.dto.ImmutableCreatePlanDto;
import com.weizilla.workouts.entity.Plan;
import com.weizilla.workouts.entity.PlanAssert;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

public class CreatePlanTest extends PlanTest {
    private CreatePlan createPlan;
    private CreatePlanDto createDto;

    @Before
    public void setUp() throws Exception {
        createDto = ImmutableCreatePlanDto.builder()
            .type(TYPE)
            .date(DATE)
            .notes(NOTES)
            .timeOfDay(TIME_OF_DAY)
            .distance(DISTANCE)
            .duration(DURATION)
            .build();
        createPlan = new CreatePlan(planStore);
    }

    @Test
    public void createShouldReturnNewRecordWithId() throws Exception {
        Plan actual = createPlan.create(createDto);
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
        PlanAssert.assertThat(actual).hasType(TYPE);
        PlanAssert.assertThat(actual).hasDate(DATE);
        PlanAssert.assertThat(actual).hasNotes(NOTES);
        PlanAssert.assertThat(actual).hasTimeOfDay(TIME_OF_DAY);
        PlanAssert.assertThat(actual).hasDuration(DURATION);
        PlanAssert.assertThat(actual).hasDistance(DISTANCE);
    }

    @Test
    public void createShouldStoreNewRecordInStore() throws Exception {
        Plan actual = createPlan.create(createDto);
        verify(planStore).add(actual);
    }
}