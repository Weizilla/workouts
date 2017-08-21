package com.weizilla.workouts.resouces;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.io.Resources;
import com.weizilla.workouts.entity.ObjectMappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

@Path("/build/")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class BuildResource {
    private static final Logger logger = LoggerFactory.getLogger(BuildResource.class);
    private static final Map<String, String> BUILD_INFO = createBuildInfo();

    @GET
    public Map<String, String> getBuildInfo() throws Exception {
        return BUILD_INFO;
    }

    private static Map<String, String> createBuildInfo() {
        try {
            URL jsonUrl = Resources.getResource("build.json");
            JsonNode node = ObjectMappers.OBJECT_MAPPER.readTree(jsonUrl);

            Map<String, String> info = new TreeMap<>();
            info.put("git.commit.id.abbrev", node.path("git.commit.id.abbrev").textValue());
            info.put("git.build.time", node.path("git.build.time").textValue());

            return info;
        } catch (Exception e) {
            logger.error("Cannot create build info: {}", e.getMessage(), e);
        }

        return Collections.emptyMap();
    }
}
