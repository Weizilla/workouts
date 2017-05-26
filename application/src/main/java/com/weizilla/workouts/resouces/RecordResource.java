package com.weizilla.workouts.resouces;

import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.jdbi.RecordDao;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/records")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecordResource {

    public RecordResource(RecordDao dao) {
    }

    @POST
    public void add(@NotNull Record record) {
    }

    @GET
    public List<Record> getAll() {
        return null;
    }
}
