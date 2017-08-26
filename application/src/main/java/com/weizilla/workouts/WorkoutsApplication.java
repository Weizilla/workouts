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
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.skife.jdbi.v2.DBI;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;
import java.util.EnumSet;

public class WorkoutsApplication extends Application<WorkoutsConfiguration> {

    public static void main(String[] args) throws Exception {
        new WorkoutsApplication().run(args);
    }

    @Override
    public String getName() {
        return "Workouts";
    }

    @Override
    public void initialize(Bootstrap<WorkoutsConfiguration> bootstrap) {
        bootstrap.setObjectMapper(ObjectMappers.OBJECT_MAPPER);
        bootstrap.addBundle(new ConfiguredAssetsBundle("/public/", "/"));
    }

    @Override
    public void run(WorkoutsConfiguration configuration,
                    Environment environment) throws Exception {
        // Configure CORS parameters
        Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        cors.setInitParameter("allowedOrigins", "http://localhost:8000");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");


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
