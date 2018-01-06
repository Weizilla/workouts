package com.weizilla.workouts.jdbi.dao;

import com.weizilla.distance.Distance;
import com.weizilla.workouts.entity.Activity;
import com.weizilla.workouts.entity.ImmutableActivity;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

public class ActivityMapper implements ResultSetMapper<Activity> {
    @Override
    public Activity map(int index, ResultSet resultSet, StatementContext ctx) throws SQLException {
        return ImmutableActivity.builder()
            .id(resultSet.getLong("id"))
            .type(resultSet.getString("type"))
            .startTime(LocalDateTime.parse(resultSet.getString("start_time")))
            .duration(Duration.ofSeconds(resultSet.getLong("duration")))
            .distance(Distance.ofMeters(resultSet.getLong("distance")))
            .createdTime(Instant.ofEpochSecond(resultSet.getLong("created_time")))
            .build();
    }
}
