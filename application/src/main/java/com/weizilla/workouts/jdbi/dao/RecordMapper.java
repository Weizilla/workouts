package com.weizilla.workouts.jdbi.dao;

import com.weizilla.workouts.entity.ImmutableRecord;
import com.weizilla.workouts.entity.Record;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class RecordMapper implements ResultSetMapper<Record> {
    @Override
    public Record map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {

        Record record = ImmutableRecord.builder()
            .id(UUID.fromString(resultSet.getString("id")))
            .type(resultSet.getString("type"))
            .outdoor(resultSet.getBoolean("outdoor"))
            .comment(resultSet.getString("comment"))
            .date(LocalDate.parse(resultSet.getString("date")))
            .duration(DaoUtils.getOptionalDuration(resultSet))
            .distance(DaoUtils.getOptionalDistance(resultSet))
            .rating(resultSet.getInt("rating"))
            .createdTime(Instant.ofEpochSecond(resultSet.getLong("created_time")))
            .build();
        return record;
    }
}
