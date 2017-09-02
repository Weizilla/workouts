package com.weizilla.workouts.resouces;

import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.interactor.CreateRecord;
import com.weizilla.workouts.interactor.DeleteRecord;
import com.weizilla.workouts.interactor.GetRecords;
import com.weizilla.workouts.interactor.UpdateRecord;
import io.dropwizard.jersey.jsr310.LocalDateParam;

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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Path("/records/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class RecordResource {
    private final CreateRecord createRecord;
    private final GetRecords getRecords;
    private final DeleteRecord deleteRecord;
    private final UpdateRecord updateRecord;

    @Inject
    public RecordResource(CreateRecord createRecord, GetRecords getRecords,
            DeleteRecord deleteRecord, UpdateRecord updateRecord) {
        this.createRecord = createRecord;
        this.getRecords = getRecords;
        this.deleteRecord = deleteRecord;
        this.updateRecord = updateRecord;
    }

    @POST
    public Record add(@NotNull Record record) {
        return createRecord.create(record);
    }

    @GET
    public List<Record> getByDate(@QueryParam("date") Optional<LocalDateParam> date) {
        return date.map(d -> getRecords.get(d.get())).orElseGet(getRecords::getAll);
    }

    @GET
    @Path("{id}")
    public Record getById(@PathParam("id") UUID id) {
        return getRecords.get(id);
    }

    @PUT
    public void update(@NotNull Record record) {
        updateRecord.updateRecord(record);
    }

    @DELETE
    @Path("{id}")
    public void deleteById(@PathParam("id") UUID id) {
        deleteRecord.delete(id);
    }
}
