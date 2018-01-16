package com.weizilla.workouts.resouces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Sets;
import com.weizilla.workouts.entity.ObjectMappers;
import com.weizilla.workouts.interactor.GetTypes;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TypesResourceTest {
    private static final GetTypes GET_TYPES = mock(GetTypes.class);
    private static final TypeReference<Set<String>> TYPE_REF = new TypeReference<Set<String>>() { };

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
        .addResource(new TypesResource(GET_TYPES))
        .setMapper(ObjectMappers.OBJECT_MAPPER)
        .build();

    @After
    public void tearDown() throws Exception {
        reset(GET_TYPES);
    }

    @Test
    public void getsTypes() throws Exception {
        Set<String> types = Sets.newHashSet("TYPE 1", "TYPE 2", "TYPE 3");
        when(GET_TYPES.get()).thenReturn(types);
        String jsonResult = RESOURCES.target("/types/").request().get(String.class);
        Set<String> results = ObjectMappers.OBJECT_MAPPER.readValue(jsonResult, TYPE_REF);
        assertThat(results).isEqualTo(types);
    }
}
