package com.weizilla.workouts;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.weizilla.workouts.config.WorkoutsConfiguration;
import com.weizilla.workouts.entity.ObjectMappers;
import com.weizilla.workouts.guice.WorkoutsModule;
import com.weizilla.workouts.jdbi.ActivityDao;
import com.weizilla.workouts.jdbi.LocalDateArgumentFactory;
import com.weizilla.workouts.jdbi.LocalDateTimeArgumentFactory;
import com.weizilla.workouts.jdbi.RecordDao;
import com.weizilla.workouts.resouces.ActivityResource;
import com.weizilla.workouts.resouces.BuildResource;
import io.dropwizard.Application;
import io.dropwizard.bundles.assets.ConfiguredAssetsBundle;
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
        bootstrap.addBundle(new ConfiguredAssetsBundle("/public/", "/"));
    }

    @Override
    public void run(final WorkoutsConfiguration configuration,
                    final Environment environment) throws Exception {
        DBIFactory factory = new DBIFactory();
        DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "sqlite");
        jdbi.registerArgumentFactory(new LocalDateArgumentFactory());
        jdbi.registerArgumentFactory(new LocalDateTimeArgumentFactory());

        RecordDao recordDao = jdbi.onDemand(RecordDao.class);
        recordDao.createTable();

        ActivityDao activityDao = jdbi.onDemand(ActivityDao.class);
        activityDao.createTable();

        WorkoutsModule module = new WorkoutsModule(configuration.getGarmin().getUrlBases(),
            configuration.getGarmin().getCredentials(), activityDao);

        Injector injector = Guice.createInjector(module);

        ActivityResource activityResource = injector.getInstance(ActivityResource.class);
        environment.jersey().register(activityResource);

        environment.jersey().register(new BuildResource());
    }

}
