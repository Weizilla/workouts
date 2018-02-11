package com.weizilla.workouts.resouces;

import com.weizilla.workouts.entity.WorkoutStat;
import com.weizilla.workouts.interactor.GenerateWorkoutStat;
import io.dropwizard.jersey.jsr310.LocalDateParam;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("/workouts/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class WorkoutResource {
    private final GenerateWorkoutStat generateWorkoutStat;

    @Inject
    public WorkoutResource(GenerateWorkoutStat generateWorkoutStat) {
        this.generateWorkoutStat = generateWorkoutStat;
    }

    @GET
    public List<WorkoutStat> get(@QueryParam("date") Optional<LocalDateParam> date) {
        return date.map(d -> generateWorkoutStat.get(d.get())).orElse(generateWorkoutStat.getAll());
    }
}
