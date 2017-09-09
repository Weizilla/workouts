package com.weizilla.workouts.resouces;

import com.weizilla.workouts.entity.Activity;
import com.weizilla.workouts.entity.Export;
import com.weizilla.workouts.entity.ObjectMappers;
import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.entity.TestEntity;
import com.weizilla.workouts.store.GarminStore;
import com.weizilla.workouts.store.MemoryGarminStore;
import com.weizilla.workouts.store.MemoryRecordStore;
import com.weizilla.workouts.store.RecordStore;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class ExportResourceTest {
    private static final Activity ACTIVITY = TestEntity.createActivity();
    private static final Record RECORD = TestEntity.createRecord();
    private static final GarminStore GARMIN_STORE = new MemoryGarminStore();
    private static final RecordStore RECORD_STORE = new MemoryRecordStore();

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
        .addResource(new ExportResource(GARMIN_STORE, RECORD_STORE))
        .setMapper(ObjectMappers.OBJECT_MAPPER)
        .build();

    @BeforeClass
    public static void setUp() throws Exception {
        GARMIN_STORE.add(ACTIVITY);
        RECORD_STORE.add(RECORD);
    }

    @Test
    public void getsAllDataForExport() throws Exception {
        Instant now = Instant.now();
        Export export = RESOURCES.target("/export").request().get(Export.class);
        assertThat(export.getActivities()).containsExactly(ACTIVITY);
        assertThat(export.getRecords()).containsExactly(RECORD);
        assertThat(export.getGenerated()).isBetween(now.minusSeconds(10), now.plusSeconds(10));
    }
}