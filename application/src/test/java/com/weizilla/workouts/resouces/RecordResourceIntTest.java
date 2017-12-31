package com.weizilla.workouts.resouces;

import com.weizilla.workouts.WorkoutsApplication;
import com.weizilla.workouts.config.WorkoutsConfiguration;
import com.weizilla.workouts.entity.ImmutableRecord;
import com.weizilla.workouts.entity.ObjectMappers;
import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.entity.TestEntity;
import com.weizilla.workouts.test.TestUtils;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.weizilla.workouts.entity.TestEntity.DATE;
import static com.weizilla.workouts.entity.TestEntity.RECORD_ID;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

public class RecordResourceIntTest {
    private static final Record RECORD = TestEntity.createRecord();

    @ClassRule
    public static final DropwizardAppRule<WorkoutsConfiguration> RULE =
        new DropwizardAppRule<>(WorkoutsApplication.class, ResourceHelpers.resourceFilePath("test-config.yml"));
    private static String baseUrl;
    private static Client client;
    private static DBI dbi;

    @BeforeClass
    public static void setUp() {
        baseUrl = String.format("http://localhost:%d/api/records/", RULE.getLocalPort());
        client = new JerseyClientBuilder(RULE.getEnvironment()).build("client");
        dbi = new DBIFactory().build(RULE.getEnvironment(),
            RULE.getConfiguration().getDataSourceFactory(), "sqlite");
    }

    @After
    public void tearDown() {
        try (Handle handle = dbi.open()) {
            handle.execute("DELETE FROM records");
        }
    }

    @Test
    public void addsRecord() throws Exception {
        Response response = client.target(baseUrl).request().post(toEntity(RECORD));

        assertThat(response.getStatus()).isEqualTo(SC_OK);
        InputStream responseStream = (InputStream) response.getEntity();
        Record result = ObjectMappers.OBJECT_MAPPER.readValue(responseStream, Record.class);
        assertThat(result).isEqualTo(RECORD);
    }

    @Test
    public void getsAllRecords() throws Exception {
        Response createResponse = client.target(baseUrl).request().post(toEntity(RECORD));
        assertThat(createResponse.getStatus()).isEqualTo(SC_OK);

        Response getsAllResponse = client.target(baseUrl).request().get();
        assertThat(getsAllResponse.getStatus()).isEqualTo(SC_OK);

        List<Record> results = readRecordList(getsAllResponse);
        assertThat(results).containsExactly(RECORD);
    }

    @Test
    public void getsRecordByDate() throws Exception {
        Response createResponse = client.target(baseUrl).request().post(toEntity(RECORD));
        assertThat(createResponse.getStatus()).isEqualTo(SC_OK);

        Response getsAllResponse = client.target(baseUrl).queryParam("date", DATE).request().get();
        assertThat(getsAllResponse.getStatus()).isEqualTo(SC_OK);

        List<Record> results = readRecordList(getsAllResponse);
        assertThat(results).containsExactly(RECORD);
    }

    @Test
    public void getsRecordById() throws Exception {
        Response createResponse = client.target(baseUrl).request().post(toEntity(RECORD));
        assertThat(createResponse.getStatus()).isEqualTo(SC_OK);

        Response getsAllResponse = client.target(baseUrl).queryParam("id", RECORD_ID).request().get();
        assertThat(getsAllResponse.getStatus()).isEqualTo(SC_OK);

        List<Record> results = readRecordList(getsAllResponse);
        assertThat(results).containsExactly(RECORD);
    }

    @Test
    public void updatesRecord() throws Exception {
        Response createResponse = client.target(baseUrl).request().post(toEntity(RECORD));
        assertThat(createResponse.getStatus()).isEqualTo(SC_OK);

        Response getsAllResponse = client.target(baseUrl).request().get();
        assertThat(getsAllResponse.getStatus()).isEqualTo(SC_OK);

        List<Record> results = readRecordList(getsAllResponse);
        assertThat(results).containsExactly(RECORD);

        String newType = "new type";
        Record updatedRecord = ImmutableRecord.copyOf(RECORD).withType(newType);

        Response updateResponse = client.target(baseUrl).request().put(toEntity(updatedRecord));
        assertThat(updateResponse.getStatus()).isEqualTo(SC_OK);

        getsAllResponse = client.target(baseUrl).request().get();
        assertThat(getsAllResponse.getStatus()).isEqualTo(SC_OK);

        results = readRecordList(getsAllResponse);
        assertThat(results).containsExactly(updatedRecord);
    }


    @Test
    public void deletesRecord() throws Exception {
        Response createResponse = client.target(baseUrl).request().post(toEntity(RECORD));
        assertThat(createResponse.getStatus()).isEqualTo(SC_OK);

        Response getsAllResponse = client.target(baseUrl).request().get();
        assertThat(getsAllResponse.getStatus()).isEqualTo(SC_OK);

        List<Record> results = readRecordList(getsAllResponse);
        assertThat(results).containsExactly(RECORD);

        Response deleteResponse = client.target(baseUrl + RECORD_ID).request().delete();
        assertThat(deleteResponse.getStatus()).isEqualTo(SC_OK);

        getsAllResponse = client.target(baseUrl).request().get();
        assertThat(getsAllResponse.getStatus()).isEqualTo(SC_OK);

        results = readRecordList(getsAllResponse);
        assertThat(results).isEmpty();
    }

    private static Entity<Record> toEntity(Record record) {
        return Entity.entity(record, MediaType.APPLICATION_JSON_TYPE);
    }

    private static List<Record> readRecordList(Response response) throws IOException {
        InputStream responseStream = (InputStream) response.getEntity();
        return ObjectMappers.OBJECT_MAPPER.readValue(responseStream, TestUtils.LIST_RECORDS);
    }
}