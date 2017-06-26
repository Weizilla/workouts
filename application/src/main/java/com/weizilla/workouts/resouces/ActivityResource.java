package com.weizilla.workouts.resouces;

import com.weizilla.garmin.entity.Activity;
import com.weizilla.workouts.interactor.GetActivities;
import com.weizilla.workouts.interactor.UpdateGarminStore;
import io.dropwizard.jersey.jsr310.LocalDateParam;

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
public class ActivityResource {
    private final GetActivities getActivities;
    private final UpdateGarminStore updateGarminStore;

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
    public Map<String, Integer> updateGarminStore() throws Exception {
        int downloaded = updateGarminStore.update();
        return Collections.singletonMap("downloaded", downloaded);
    }
}
