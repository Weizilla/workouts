package com.weizilla.workouts.entity;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ObjectMappersTest {
    @Parameters
    public static Collection<Entry<?>> getData() {
        return Lists.newArrayList(
            TestEntity.createWorkout(),
            TestEntity.createActivity(),
            TestEntity.createRecord(),
            TestEntity.createGoal()
        );
    }

    private final Object entity;

    public ObjectMappersTest(Object entity) {
        this.entity = entity;
    }

    @Test
    public void serializesEntity() throws Exception {
        String actual = ObjectMappers.OBJECT_MAPPER.writeValueAsString(entity);
        assertThat(actual).isNotEmpty();
    }

    @Test
    public void roundTripSerialization() throws Exception {
        String json = ObjectMappers.OBJECT_MAPPER.writeValueAsString(entity);
        assertThat(json).isNotEmpty();

        Object pojo = ObjectMappers.OBJECT_MAPPER.readValue(json, entity.getClass());
        assertThat(pojo).isEqualTo(entity);
    }
}