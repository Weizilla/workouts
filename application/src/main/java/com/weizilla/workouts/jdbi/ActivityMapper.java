package com.weizilla.workouts.jdbi;

import com.weizilla.garmin.entity.Activity;
import com.weizilla.garmin.entity.ImmutableActivity;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import tec.uom.se.quantity.Quantities;

import javax.measure.quantity.Length;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;

public class ActivityMapper implements ResultSetMapper<Activity> {
    @Override
    public Activity map(int index, ResultSet resultSet, StatementContext ctx) throws SQLException {
        return ImmutableActivity.builder()
            .id(resultSet.getLong("id"))
            .type(resultSet.getString("type"))
            .start(LocalDateTime.parse(resultSet.getString("start")))
            .duration(Duration.parse(resultSet.getString("duration")))
            .distance(Quantities.getQuantity(resultSet.getString("distance")).asType(Length.class))
            .build();
    }
}
