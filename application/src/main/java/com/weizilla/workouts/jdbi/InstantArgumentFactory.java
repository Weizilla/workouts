package com.weizilla.workouts.jdbi;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.Argument;
import org.skife.jdbi.v2.tweak.ArgumentFactory;

import java.time.Instant;

public class InstantArgumentFactory implements ArgumentFactory<Instant> {
    @Override
    public boolean accepts(Class<?> aClass, Object o, StatementContext statementContext) {
        return o != null && Instant.class.isAssignableFrom(o.getClass());
    }

    @Override
    public Argument build(Class<?> aClass, Instant instant, StatementContext statementContext) {
        return (i, preparedStatement, statementContext1) -> preparedStatement.setLong(i, instant.getEpochSecond());
    }
}
