package com.weizilla.workouts.jdbi.dbi;

import com.weizilla.distance.Distance;

public class OptionalDistanceArgumentFactory extends OptionalArgumentFactory<Distance> {
    @Override
    protected Class<Distance> getValueClass() {
        return Distance.class;
    }

    @Override
    protected long getDbValue(Distance object) {
        return object.getDistanceMeter();
    }
}
