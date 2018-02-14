package com.weizilla.workouts.jdbi.dao;

import com.weizilla.distance.Distance;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Optional;

public class DaoUtils {
    public static Optional<Long> getOptionalLong(ResultSet resultSet, String key) throws SQLException {
        long value = resultSet.getLong(key);
        return resultSet.wasNull() ? Optional.empty() : Optional.of(value);
    }

    public static Optional<Duration> getOptionalDuration(ResultSet resultSet) throws SQLException {
        return getOptionalLong(resultSet, "duration").map(Duration::ofSeconds);
    }

    public static Optional<Distance> getOptionalDistance(ResultSet resultSet) throws SQLException {
        return getOptionalLong(resultSet, "distance").map(Distance::ofMeters);
    }
}
