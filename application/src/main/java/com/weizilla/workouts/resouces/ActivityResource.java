package com.weizilla.workouts.resouces;

import com.weizilla.garmin.entity.Activity;
import com.weizilla.workouts.interactor.GetActivities;
import com.weizilla.workouts.interactor.UpdateGarminStore;
import io.dropwizard.jersey.jsr310.LocalDateParam;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Path("/activities/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class ActivityResource {
    private final GetActivities getActivities;
    private final UpdateGarminStore updateGarminStore;

    @Inject
    public ActivityResource(GetActivities getActivities, UpdateGarminStore updateGarminStore) {
        this.getActivities = getActivities;
        this.updateGarminStore = updateGarminStore;
    }

    @GET
    public List<Activity> getByDate(@QueryParam("date") Optional<LocalDateParam> date) {
        return date.map(d -> getActivities.get(d.get())).orElseGet(getActivities::getAll);
    }

    @GET
    @Path("update")
    public Map<String, Object> updateGarminStore() throws Exception {
        updateGarminStore.startUpdate();
        //TODO job tracking
        return Collections.singletonMap("success", "true");
    }
}
