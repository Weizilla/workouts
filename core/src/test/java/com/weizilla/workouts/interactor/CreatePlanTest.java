package com.weizilla.workouts.interactor;

import com.weizilla.workouts.dto.CreatePlanDto;
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
        createDto = new CreatePlanDto();
        createPlan = new CreatePlan(planStore);
    }

    @Test
    public void createShouldReturnNewRecordWithId() throws Exception {
        createDto.setType(TYPE);
        createDto.setDate(DATE);
        createDto.setNotes(NOTES);
        createDto.setTimeOfDay(TIME_OF_DAY);
        createDto.setDistance(DISTANCE);
        createDto.setDuration(DURATION);

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
        createDto.setType(TYPE);
        createDto.setDate(DATE);
        createDto.setNotes(NOTES);
        createDto.setTimeOfDay(TIME_OF_DAY);
        createDto.setDistance(DISTANCE);
        createDto.setDuration(DURATION);

        Plan actual = createPlan.create(createDto);
        verify(planStore).add(actual);
    }
}