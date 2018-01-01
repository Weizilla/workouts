package com.weizilla.workouts.resouces;

import com.weizilla.workouts.WorkoutsApplication;
import com.weizilla.workouts.config.WorkoutsConfiguration;
import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.entity.ImmutableGoal;
import com.weizilla.workouts.entity.ObjectMappers;
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
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.weizilla.workouts.entity.TestEntity.DATE;
import static com.weizilla.workouts.entity.TestEntity.GOAL_ID;
import static com.weizilla.workouts.test.TestUtils.toEntity;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class GoalResourceIntTest {
    private static final Goal GOAL = TestEntity.createGoal();

    @ClassRule
    public static final DropwizardAppRule<WorkoutsConfiguration> RULE =
        new DropwizardAppRule<>(WorkoutsApplication.class, ResourceHelpers.resourceFilePath("test-config.yml"));

    private static String baseUrl;
    private static Client client;
    private static DBI dbi;

    @BeforeClass
    public static void setUp() {
        baseUrl = String.format("http://localhost:%d/api/goals/", RULE.getLocalPort());
        client = new JerseyClientBuilder(RULE.getEnvironment()).build("client");
        dbi = new DBIFactory().build(RULE.getEnvironment(),
            RULE.getConfiguration().getDataSourceFactory(), "sqlite");
    }

    @After
    public void tearDown() {
        try (Handle handle = dbi.open()) {
            handle.execute("DELETE FROM goals");
        }
    }

    @Test
    public void addsGoal() throws Exception {
        Response response = client.target(baseUrl).request().post(toEntity(GOAL));

        assertThat(response.getStatus()).isEqualTo(SC_OK);
        InputStream responseStream = (InputStream) response.getEntity();
        Goal result = ObjectMappers.OBJECT_MAPPER.readValue(responseStream, Goal.class);
        assertThat(result).isEqualTo(GOAL);
    }

    @Test
    public void getsAllGoals() throws Exception {
        Response createResponse = client.target(baseUrl).request().post(toEntity(GOAL));
        assertThat(createResponse.getStatus()).isEqualTo(SC_OK);

        Response getsAllResponse = client.target(baseUrl).request().get();
        assertThat(getsAllResponse.getStatus()).isEqualTo(SC_OK);

        List<Goal> results = readGoalList(getsAllResponse);
        assertThat(results).containsExactly(GOAL);
    }

    @Test
    public void getsGoalByDate() throws Exception {
        Response createResponse = client.target(baseUrl).request().post(toEntity(GOAL));
        assertThat(createResponse.getStatus()).isEqualTo(SC_OK);

        Response getsAllResponse = client.target(baseUrl).queryParam("date", DATE).request().get();
        assertThat(getsAllResponse.getStatus()).isEqualTo(SC_OK);

        List<Goal> results = readGoalList(getsAllResponse);
        assertThat(results).containsExactly(GOAL);
    }

    @Test
    public void getsGoalById() throws Exception {
        Response createResponse = client.target(baseUrl).request().post(toEntity(GOAL));
        assertThat(createResponse.getStatus()).isEqualTo(SC_OK);

        Response getsAllResponse = client.target(baseUrl).queryParam("id", GOAL_ID).request().get();
        assertThat(getsAllResponse.getStatus()).isEqualTo(SC_OK);

        List<Goal> results = readGoalList(getsAllResponse);
        assertThat(results).containsExactly(GOAL);
    }

    @Test
    public void updatesGoal() throws Exception {
        Response createResponse = client.target(baseUrl).request().post(toEntity(GOAL));
        assertThat(createResponse.getStatus()).isEqualTo(SC_OK);

        Response getsAllResponse = client.target(baseUrl).request().get();
        assertThat(getsAllResponse.getStatus()).isEqualTo(SC_OK);

        List<Goal> results = readGoalList(getsAllResponse);
        assertThat(results).containsExactly(GOAL);

        String newType = "new type";
        Goal updatedGoal = ImmutableGoal.copyOf(GOAL).withType(newType);

        Response updateResponse = client.target(baseUrl).request().put(toEntity(updatedGoal));
        assertThat(updateResponse.getStatus()).isEqualTo(SC_OK);

        getsAllResponse = client.target(baseUrl).request().get();
        assertThat(getsAllResponse.getStatus()).isEqualTo(SC_OK);

        results = readGoalList(getsAllResponse);
        assertThat(results).containsExactly(updatedGoal);
    }

    @Test
    public void deletesGoal() throws Exception {
        Response createResponse = client.target(baseUrl).request().post(toEntity(GOAL));
        assertThat(createResponse.getStatus()).isEqualTo(SC_OK);

        Response getsAllResponse = client.target(baseUrl).request().get();
        assertThat(getsAllResponse.getStatus()).isEqualTo(SC_OK);

        List<Goal> results = readGoalList(getsAllResponse);
        assertThat(results).containsExactly(GOAL);

        Response deleteResponse = client.target(baseUrl + GOAL_ID).request().delete();
        assertThat(deleteResponse.getStatus()).isEqualTo(SC_OK);

        getsAllResponse = client.target(baseUrl).request().get();
        assertThat(getsAllResponse.getStatus()).isEqualTo(SC_OK);

        results = readGoalList(getsAllResponse);
        assertThat(results).isEmpty();
    }

    private static List<Goal> readGoalList(Response response) throws IOException {
        InputStream responseStream = (InputStream) response.getEntity();
        return ObjectMappers.OBJECT_MAPPER.readValue(responseStream, TestUtils.LIST_GOALS);
    }
}
