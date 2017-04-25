package com.weizilla.workouts.resouces;

import com.weizilla.workouts.jdbi.RecordDao;
import com.weizilla.workouts.entity.Record;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

@Path("/records")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecordResource {
    private final RecordDao dao;

    public RecordResource(RecordDao dao) {
        this.dao = dao;
    }

    @POST
    public void add(@NotNull Record record) {
        UUID id = UUID.randomUUID();
        dao.insert(record);
    }

    @GET
    public List<Record> getAll() {
        return dao.getAll();
    }
}
