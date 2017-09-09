package com.weizilla.workouts.resouces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.weizilla.workouts.entity.ObjectMappers;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class BuildResourceTest {
    private static final TypeReference<Map<String, String>> TYPE_REF = new TypeReference<Map<String, String>>() {};

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
        .addResource(new BuildResource())
        .setMapper(ObjectMappers.OBJECT_MAPPER)
        .build();


    @Test
    public void getsBuildInformation() throws Exception {
        String jsonResult = RESOURCES.target("/build/").request().get(String.class);
        assertThat(jsonResult).isNotNull();
    }

    @Test
    public void getsBuildTime() throws Exception {
        String jsonResult = RESOURCES.target("/build/").request().get(String.class);
        Map<String, String> actual = ObjectMappers.OBJECT_MAPPER.readValue(jsonResult, TYPE_REF);
        assertThat(actual).containsKeys("git.build.time");
        assertThat(actual.get("git.build.time")).isNotNull();
    }

    @Test
    public void getsCommitIdAbbrev() throws Exception {
        String jsonResult = RESOURCES.target("/build/").request().get(String.class);
        Map<String, String> actual = ObjectMappers.OBJECT_MAPPER.readValue(jsonResult, TYPE_REF);
        assertThat(actual).containsKeys("git.commit.id.abbrev");
        assertThat(actual.get("git.commit.id.abbrev")).isNotNull();
    }
}