package com.weizilla.workouts.jdbi;

import com.weizilla.distance.Distance;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.Argument;
import org.skife.jdbi.v2.tweak.ArgumentFactory;

public class DistanceArgumentFactory implements ArgumentFactory<Distance> {
    @Override
    public boolean accepts(Class<?> aClass, Object o, StatementContext statementContext) {
        return o != null && Distance.class.isAssignableFrom(o.getClass());
    }

    @Override
    public Argument build(Class<?> aClass, Distance distance, StatementContext statementContext) {
        return (i, preparedStatement, statementContext1) -> preparedStatement.setLong(i, distance.getDistanceMeter());
    }
}
