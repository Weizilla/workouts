package com.weizilla.workouts.resouces;

import com.weizilla.workouts.dto.CreateRecordDto;
import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.interactor.CreateRecord;
import com.weizilla.workouts.interactor.DeleteRecord;
import com.weizilla.workouts.interactor.GetRecords;
import com.weizilla.workouts.interactor.UpdateRecord;

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
    private final CreateRecord createRecord;
    private final GetRecords getRecords;
    private final DeleteRecord deleteRecord;
    private final UpdateRecord updateRecord;

    public RecordResource(CreateRecord createRecord, GetRecords getRecords,
            DeleteRecord deleteRecord, UpdateRecord updateRecord) {
        this.createRecord = createRecord;
        this.getRecords = getRecords;
        this.deleteRecord = deleteRecord;
        this.updateRecord = updateRecord;
    }

    @POST
    public void add(@NotNull CreateRecordDto record) {
        createRecord.create(record);
    }

    @GET
    public List<Record> getAll() {
        return getRecords.getAll();
    }

//    @GET
//    public List<Record> getByDate(LocalDate date) {
//        return getRecords.get(date);
//    }
//
//    @GET
//    public Record getById(UUID id) {
//        return getRecords.get(id);
//    }
//
//    @PUT
//    public void update(@NotNull Record record) {
//        updateRecord.updateRecord(record);
//    }
//
//    @DELETE
//    public void deleteById(UUID id) {
//        deleteRecord.delete(id);
//    }
}
