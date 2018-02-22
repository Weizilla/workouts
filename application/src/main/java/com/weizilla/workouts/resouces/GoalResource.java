package com.weizilla.workouts.resouces;

import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.interactor.CreateGoal;
import com.weizilla.workouts.interactor.DeleteGoal;
import com.weizilla.workouts.interactor.GetGoals;
import com.weizilla.workouts.interactor.UpdateGoal;
import io.dropwizard.jersey.jsr310.LocalDateParam;
import io.dropwizard.jersey.params.AbstractParam;
import io.dropwizard.jersey.params.IntParam;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Path("/goals/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class GoalResource {
    private final CreateGoal createGoal;
    private final GetGoals getGoals;
    private final DeleteGoal deleteGoal;
    private final UpdateGoal updateGoal;

    @Inject
    public GoalResource(CreateGoal createGoal, GetGoals getGoals,
        DeleteGoal deleteGoal, UpdateGoal updateGoal) {
        this.createGoal = createGoal;
        this.getGoals = getGoals;
        this.deleteGoal = deleteGoal;
        this.updateGoal = updateGoal;
    }


    @POST
    public Goal add(@NotNull Goal record) {
        return createGoal.create(record);
    }

    @GET
    public List<Goal> getByDate(@QueryParam("date") Optional<LocalDateParam> date,
            @QueryParam("numDays") Optional<IntParam> numDays) {

        List<Goal> goals;

        if (date.isPresent() || numDays.isPresent()) {
            LocalDate startDate = date.map(LocalDateParam::get).orElse(LocalDate.now());
            int num = numDays.map(AbstractParam::get).orElse(1);
            goals = getGoals.get(startDate, num);
        } else {
            goals = getGoals.getAll();
        }

        return goals;
    }

    @GET
    @Path("{id}")
    public Goal getById(@PathParam("id") UUID id) {
        return getGoals.get(id);
    }

    @PUT
    public Goal update(@NotNull Goal record) {
        return updateGoal.updateGoal(record);
    }

    @DELETE
    @Path("{id}")
    public UUID deleteById(@PathParam("id") UUID id) {
        return deleteGoal.delete(id);
    }
}
