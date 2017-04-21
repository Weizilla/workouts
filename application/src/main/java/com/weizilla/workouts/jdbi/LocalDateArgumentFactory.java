package com.weizilla.workouts.jdbi;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.Argument;
import org.skife.jdbi.v2.tweak.ArgumentFactory;

import java.time.LocalDate;

public class LocalDateArgumentFactory implements ArgumentFactory<LocalDate> {
    @Override
    public boolean accepts(Class<?> aClass, Object o, StatementContext statementContext) {
        return o != null && LocalDate.class.isAssignableFrom(o.getClass());
    }

    @Override
    public Argument build(Class<?> aClass, LocalDate localDate, StatementContext statementContext) {
        return (i, preparedStatement, statementContext1) -> preparedStatement.setString(i, localDate.toString());
    }
}
