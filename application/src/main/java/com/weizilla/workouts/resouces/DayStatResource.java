package com.weizilla.workouts.resouces;

import com.weizilla.workouts.entity.DayStat;
import com.weizilla.workouts.interactor.WorkoutStatGenerator;
import io.dropwizard.jersey.jsr310.LocalDateParam;
import io.dropwizard.jersey.params.AbstractParam;
import io.dropwizard.jersey.params.IntParam;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
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
    public List<DayStat> get(@QueryParam("date") Optional<LocalDateParam> date,
            @QueryParam("numDays") Optional<IntParam> numDays) {

        List<DayStat> stats;

        if (date.isPresent() || numDays.isPresent()) {
            LocalDate startDate = date.map(LocalDateParam::get).orElse(LocalDate.now());
            int num = numDays.map(AbstractParam::get).orElse(1);
            stats = workoutStatGenerator.generate(startDate, num);
        } else {
            stats = workoutStatGenerator.generateAll();
        }
        return stats;
    }

    @GET
    @Path("{date}")
    public Optional<DayStat> getByDate(@PathParam("date") LocalDateParam date) {
        return workoutStatGenerator.generate(date.get());
    }
}
