package com.weizilla.workouts.jdbi.dbi;

import java.time.Duration;

public class OptionalDurationArgumentFactory extends OptionalArgumentFactory<Duration> {
    @Override
    protected Class<Duration> getValueClass() {
        return Duration.class;
    }

    @Override
    protected long getDbValue(Duration object) {
        return object.getSeconds();
    }
}
