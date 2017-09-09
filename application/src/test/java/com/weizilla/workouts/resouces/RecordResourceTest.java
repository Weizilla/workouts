package com.weizilla.workouts.resouces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.weizilla.workouts.entity.ObjectMappers;
import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.entity.TestEntity;
import com.weizilla.workouts.interactor.CreateRecord;
import com.weizilla.workouts.interactor.DeleteRecord;
import com.weizilla.workouts.interactor.GetRecords;
import com.weizilla.workouts.interactor.UpdateRecord;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static com.weizilla.workouts.entity.TestEntity.DATE;
import static com.weizilla.workouts.entity.TestEntity.RECORD_ID;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecordResourceTest {
    private static final Record RECORD = TestEntity.createRecord();
    private static final CreateRecord CREATE_RECORD = mock(CreateRecord.class);
    private static final GetRecords GET_RECORDS = mock(GetRecords.class);
    private static final UpdateRecord UPDATE_RECORD = mock(UpdateRecord.class);
    private static final DeleteRecord DELETE_RECORD = mock(DeleteRecord.class);
    private static final TypeReference<List<Record>> TYPE_REF = new TypeReference<List<Record>>() { };

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
        .addResource(new RecordResource(CREATE_RECORD, GET_RECORDS, DELETE_RECORD, UPDATE_RECORD))
        .setMapper(ObjectMappers.OBJECT_MAPPER)
        .build();

    @After
    public void tearDown() throws Exception {
        reset(CREATE_RECORD, GET_RECORDS, UPDATE_RECORD, DELETE_RECORD);
    }

    @Test
    public void addsRecord() throws Exception {
        RESOURCES.target("/records").request()
            .post(Entity.entity(RECORD, MediaType.APPLICATION_JSON_TYPE));

        verify(CREATE_RECORD).create(RECORD);
    }

    @Test
    public void getsAllRecords() throws Exception {
        when(GET_RECORDS.getAll()).thenReturn(singletonList(RECORD));
        String jsonResult = RESOURCES.target("/records").request().get(String.class);
        List<Record> results = ObjectMappers.OBJECT_MAPPER.readValue(jsonResult, TYPE_REF);
        assertThat(results).containsExactly(RECORD);
    }

    @Test
    public void getsRecordByDate() throws Exception {
        when(GET_RECORDS.get(DATE)).thenReturn(singletonList(RECORD));

        String jsonResult = RESOURCES.target("/records")
            .queryParam("date", DATE).request().get(String.class);
        List<Record> results = ObjectMappers.OBJECT_MAPPER.readValue(jsonResult, TYPE_REF);
        assertThat(results).containsExactly(RECORD);
    }

    @Test
    public void getsRecordById() throws Exception {
        when(GET_RECORDS.get(RECORD_ID)).thenReturn(RECORD);

        String jsonResult = RESOURCES.target("/records/" + RECORD_ID).request().get(String.class);
        Record result = ObjectMappers.OBJECT_MAPPER.readValue(jsonResult, Record.class);
        assertThat(result).isEqualTo(RECORD);
    }

    @Test
    public void updatesRecord() throws Exception {
        RESOURCES.target("/records").request()
            .put(Entity.entity(RECORD, MediaType.APPLICATION_JSON_TYPE));

        verify(UPDATE_RECORD).updateRecord(RECORD);
    }

    @Test
    public void deletesRecord() throws Exception {
        RESOURCES.target("/records/" + RECORD_ID).request().delete();

        verify(DELETE_RECORD).delete(RECORD_ID);
    }
}