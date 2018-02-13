package com.weizilla.workouts.resouces;

import com.weizilla.workouts.entity.DayStat;
import com.weizilla.workouts.interactor.WorkoutStatGenerator;
import io.dropwizard.jersey.jsr310.LocalDateParam;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("/stats/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class DayStatResource {
    private final WorkoutStatGenerator workoutStatGenerator;

    @Inject
    public DayStatResource(WorkoutStatGenerator workoutStatGenerator) {
        this.workoutStatGenerator = workoutStatGenerator;
    }

    @GET
    public List<DayStat> getAll() {
        return workoutStatGenerator.generateAll();
    }

    @GET
    @Path("{date}")
    public Optional<DayStat> getByDate(@PathParam("date") LocalDateParam date) {
        return workoutStatGenerator.generate(date.get());
    }
}
