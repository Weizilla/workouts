package com.weizilla.workouts.jdbi.dbi;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.Argument;
import org.skife.jdbi.v2.tweak.ArgumentFactory;

import java.util.Optional;

public abstract class OptionalArgumentFactory<T> implements ArgumentFactory<Optional<T>> {
    protected abstract Class<T> getValueClass();

    protected abstract long getDbValue(T object);

    @Override
    public boolean accepts(Class<?> expectedType, Object value, StatementContext ctx) {
        if (value != null && Optional.class.isAssignableFrom(value.getClass())) {
            Optional optional = (Optional) value;
            return optional.isPresent() && getValueClass().isAssignableFrom(optional.get().getClass());
        }
        return false;
    }

    @Override
    public Argument build(Class<?> expectedType, Optional<T> value, StatementContext ctx) {
        long dbValue = getDbValue(value.get());
        return (i, preparedStatement, statementContext1) -> preparedStatement.setLong(i, dbValue);
    }
}
