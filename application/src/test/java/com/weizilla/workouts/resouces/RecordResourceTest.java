package com.weizilla.workouts.resouces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.weizilla.distance.Distance;
import com.weizilla.workouts.dto.CreateRecordDto;
import com.weizilla.workouts.dto.ImmutableCreateRecordDto;
import com.weizilla.workouts.entity.ImmutableRecord;
import com.weizilla.workouts.entity.ObjectMappers;
import com.weizilla.workouts.entity.Record;
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
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecordResourceTest {
    protected static final UUID ID = UUID.randomUUID();
    protected static final String TYPE = "TYPE";
    protected static final boolean OUTDOOR = true;
    protected static final LocalDate DATE = LocalDate.now();
    protected static final int RATING = 3;
    protected static final Duration DURATION = Duration.ofHours(1);
    protected static final Distance DISTANCE = Distance.ofMiles(1);
    protected static final String COMMENT = "COMMENT";
    private static final Record record = ImmutableRecord.builder()
        .id(ID)
        .type(TYPE)
        .outdoor(OUTDOOR)
        .date(DATE)
        .rating(RATING)
        .duration(DURATION)
        .distance(DISTANCE)
        .comment(COMMENT)
        .build();

    private static final CreateRecord createRecord = mock(CreateRecord.class);
    private static final GetRecords getRecords = mock(GetRecords.class);
    private static final UpdateRecord updateRecord = mock(UpdateRecord.class);
    private static final DeleteRecord deleteRecord = mock(DeleteRecord.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
        .addResource(new RecordResource(createRecord, getRecords, deleteRecord, updateRecord))
        .setMapper(ObjectMappers.OBJECT_MAPPER)
        .build();
    private static final TypeReference<List<Record>> TYPE_REF = new TypeReference<List<Record>>() {
    };

    @After
    public void tearDown() throws Exception {
        reset(createRecord, getRecords, updateRecord, deleteRecord);
    }

    @Test
    public void addsRecord() throws Exception {
        CreateRecordDto create = ImmutableCreateRecordDto.builder()
            .type(TYPE)
            .outdoor(OUTDOOR)
            .date(DATE)
            .rating(RATING)
            .duration(DURATION)
            .distance(DISTANCE)
            .comment(COMMENT)
            .build();

        resources.target("/records").request()
            .post(Entity.entity(create, MediaType.APPLICATION_JSON_TYPE));

        verify(createRecord).create(create);
    }

    @Test
    public void getsAllRecords() throws Exception {
        when(getRecords.getAll()).thenReturn(singletonList(record));
        String jsonResult = resources.target("/records").request().get(String.class);
        List<Record> results = ObjectMappers.OBJECT_MAPPER.readValue(jsonResult, TYPE_REF);
        assertThat(results).containsExactly(record);
    }

    @Test
    public void getsRecordByDate() throws Exception {
        when(getRecords.get(DATE)).thenReturn(singletonList(record));

        String jsonResult = resources.target("/records")
            .queryParam("date", DATE).request().get(String.class);
        List<Record> results = ObjectMappers.OBJECT_MAPPER.readValue(jsonResult, TYPE_REF);
        assertThat(results).containsExactly(record);
    }

    @Test
    public void getsRecordById() throws Exception {
        when(getRecords.get(ID)).thenReturn(record);

        String jsonResult = resources.target("/records/" + ID).request().get(String.class);
        Record result = ObjectMappers.OBJECT_MAPPER.readValue(jsonResult, Record.class);
        assertThat(result).isEqualTo(record);
    }

    @Test
    public void updatesRecord() throws Exception {
        resources.target("/records").request()
            .put(Entity.entity(record, MediaType.APPLICATION_JSON_TYPE));

        verify(updateRecord).updateRecord(record);
    }

    @Test
    public void deletesRecord() throws Exception {
        resources.target("/records/" + ID).request().delete();

        verify(deleteRecord).delete(ID);
    }
}