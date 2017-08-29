package com.weizilla.workouts.resouces;

import com.weizilla.workouts.entity.Export;
import com.weizilla.workouts.entity.ImmutableExport;
import com.weizilla.workouts.store.GarminStore;
import com.weizilla.workouts.store.RecordStore;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.Instant;

@Path("/export")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class ExportResource {
    private final GarminStore garminStore;
    private final RecordStore recordStore;

    @Inject
    public ExportResource(GarminStore garminStore, RecordStore recordStore) {
        this.garminStore = garminStore;
        this.recordStore = recordStore;
    }

    @GET
    public Export export() {
        return ImmutableExport.builder()
            .records(recordStore.getAll())
            .activities(garminStore.getAll())
            .generated(Instant.now())
            .build();
    }
}
