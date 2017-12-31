package com.weizilla.workouts.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.weizilla.workouts.entity.Record;

import java.util.List;

public class TestUtils {
    public static final TypeReference<List<Record>> LIST_RECORDS = new TypeReference<List<Record>>() {};
}
