package com.weizilla.workouts.resouces;

import com.weizilla.workouts.interactor.GetTypes;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Set;

@Path("/types/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class TypesResource {
    private final GetTypes getTypes;

    @Inject
    public TypesResource(GetTypes getTypes) {
        this.getTypes = getTypes;
    }

    @GET
    public Set<String> get() {
        return getTypes.get();
    }
}
