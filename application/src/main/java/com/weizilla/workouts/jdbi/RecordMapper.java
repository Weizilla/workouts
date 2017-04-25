package com.weizilla.workouts.jdbi;

import com.weizilla.workouts.entity.Record;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RecordMapper implements ResultSetMapper<Record> {
    @Override
    public Record map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
//        Record record = new Record();
//        record.setId(UUID.fromString(resultSet.getString("id")));
//        record.setComment(resultSet.getString("comment"));
//        record.setDate(LocalDate.parse(resultSet.getString("date")));
//        record.setDuration(Duration.parse(resultSet.getString("duration")));
//        record.setRating(resultSet.getInt("rating"));
//        record.setType(resultSet.getString("type"));
//        return record;
        return null;
    }
}
