package com.weizilla.workouts.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.entity.Record;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.util.List;

public class TestUtils {
    public static final TypeReference<List<Record>> LIST_RECORDS = new TypeReference<List<Record>>() {};
    public static final TypeReference<List<Goal>> LIST_GOALS = new TypeReference<List<Goal>>() {};

    public static <T> Entity<T> toEntity(T object) {
        return Entity.entity(object, MediaType.APPLICATION_JSON_TYPE);
    }
}
