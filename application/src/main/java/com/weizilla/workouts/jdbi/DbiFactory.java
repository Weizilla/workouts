package com.weizilla.workouts.jdbi;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

public class DbiFactory {

    public static DBI createDbi(Environment environment, DataSourceFactory dataSourceFactory) throws Exception {
        DBI dbi = new DBIFactory().build(environment, dataSourceFactory, "sqlite");
        dbi.registerArgumentFactory(new LocalDateArgumentFactory());
        dbi.registerArgumentFactory(new InstantArgumentFactory());
        dbi.registerArgumentFactory(new LocalDateTimeArgumentFactory());
        dbi.registerArgumentFactory(new DistanceArgumentFactory());
        dbi.registerArgumentFactory(new DurationArgumentFactory());
        return dbi;
    }
}
