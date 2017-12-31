package com.weizilla.workouts.jdbi;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.Argument;
import org.skife.jdbi.v2.tweak.ArgumentFactory;

import java.time.Duration;

public class DurationArgumentFactory implements ArgumentFactory<Duration> {
    @Override
    public boolean accepts(Class<?> aClass, Object o, StatementContext statementContext) {
        return o != null && Duration.class.isAssignableFrom(o.getClass());
    }

    @Override
    public Argument build(Class<?> aClass, Duration duration, StatementContext statementContext) {
        return (i, preparedStatement, statementContext1) -> preparedStatement.setLong(i, duration.getSeconds());
    }
}
