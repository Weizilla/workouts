package com.weizilla.workouts.resouces;

import com.weizilla.workouts.interactor.WorkoutStatGenerator;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/workouts/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class WorkoutResource {
    private final WorkoutStatGenerator workoutStatGenerator;

    @Inject
    public WorkoutResource(WorkoutStatGenerator workoutStatGenerator) {
        this.workoutStatGenerator = workoutStatGenerator;
    }

//    @GET
//    public List<WorkoutStat> get(@QueryParam("date") Optional<LocalDateParam> date) {
//        return date.map(d -> workoutStatGenerator.get(d.get())).orElse(workoutStatGenerator.getAll());
//    }
}
