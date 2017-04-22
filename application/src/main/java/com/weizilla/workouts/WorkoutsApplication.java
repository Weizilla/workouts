package com.weizilla.workouts;

import com.weizilla.workouts.entity.ObjectMappers;
import com.weizilla.workouts.jdbi.LocalDateArgumentFactory;
import com.weizilla.workouts.jdbi.RecordDao;
import com.weizilla.workouts.resouces.RecordResource;
import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

public class WorkoutsApplication extends Application<WorkoutsConfiguration> {

    public static void main(final String[] args) throws Exception {
        new WorkoutsApplication().run(args);
    }

    @Override
    public String getName() {
        return "Workouts";
    }

    @Override
    public void initialize(final Bootstrap<WorkoutsConfiguration> bootstrap) {
        bootstrap.setObjectMapper(ObjectMappers.OBJECT_MAPPER);
    }

    @Override
    public void run(final WorkoutsConfiguration configuration,
                    final Environment environment) {
        DBIFactory factory = new DBIFactory();
        DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "sqlite");
        jdbi.registerArgumentFactory(new LocalDateArgumentFactory());
        RecordDao recordDao = jdbi.onDemand(RecordDao.class);
        recordDao.createTable();
        environment.jersey().register(new RecordResource(recordDao));
    }

}
