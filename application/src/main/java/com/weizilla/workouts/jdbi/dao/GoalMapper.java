package com.weizilla.workouts.jdbi.dao;

import com.weizilla.distance.Distance;
import com.weizilla.workouts.entity.ImmutableGoal;
import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.entity.TimeOfDay;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class GoalMapper implements ResultSetMapper<Goal> {
    @Override
    public Goal map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        Goal record = ImmutableGoal.builder()
            .id(UUID.fromString(resultSet.getString("id")))
            .type(resultSet.getString("type"))
            .timeOfDay(TimeOfDay.valueOf(resultSet.getString("time_of_day")))
            .date(LocalDate.parse(resultSet.getString("date")))
            .notes(resultSet.getString("notes"))
            .duration(Duration.ofSeconds(resultSet.getLong("duration")))
            .distance(Distance.ofMeters(resultSet.getLong("distance")))
            .createdTime(Instant.ofEpochSecond(resultSet.getLong("created_time")))
            .build();
        return record;
    }
}
