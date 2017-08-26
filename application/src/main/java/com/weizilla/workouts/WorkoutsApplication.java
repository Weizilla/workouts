package com.weizilla.workouts;

import com.google.common.base.Strings;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.weizilla.garmin.configuration.Credentials;
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
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;
import java.util.EnumSet;

public class WorkoutsApplication extends Application<WorkoutsConfiguration> {
    private static final Logger logger = LoggerFactory.getLogger(WorkoutsApplication.class);

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
        // Enable variable substitution with environment variables

        bootstrap.setConfigurationSourceProvider(
            new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                new EnvironmentVariableSubstitutor()));
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

        Credentials credentials = configuration.getGarmin().getCredentials();
        checkGarminCredentials(credentials);
        WorkoutsModule module = new WorkoutsModule(configuration.getGarmin().getUrlBases(),
            credentials, activityDao);

        Injector injector = Guice.createInjector(module);
        environment.jersey().register(injector.getInstance(ActivityResource.class));
        environment.jersey().register(new BuildResource());
    }

    private static void checkGarminCredentials(Credentials credentials) {
        String username = credentials.getUsername();
        String password = credentials.getPassword();
        if (!Strings.isNullOrEmpty(username) && !Strings.isNullOrEmpty(password)) {
            logger.info("Using Garmin user {}", username);
        } else {
            if (Strings.isNullOrEmpty(username)) {
                logger.warn("Garmin username is not set");
            }
            if (Strings.isNullOrEmpty(password)) {
                logger.warn("Garmin password is not set");
            }
        }
    }
}
