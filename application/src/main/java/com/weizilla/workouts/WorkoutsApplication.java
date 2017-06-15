package com.weizilla.workouts;

import com.weizilla.workouts.entity.ObjectMappers;
import com.weizilla.workouts.interactor.CreateRecord;
import com.weizilla.workouts.interactor.DeleteRecord;
import com.weizilla.workouts.interactor.GetRecords;
import com.weizilla.workouts.interactor.UpdateRecord;
import com.weizilla.workouts.jdbi.LocalDateArgumentFactory;
import com.weizilla.workouts.jdbi.RecordDao;
import com.weizilla.workouts.resouces.BuildResource;
import com.weizilla.workouts.resouces.RecordResource;
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
                    final Environment environment) {
        DBIFactory factory = new DBIFactory();
        DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "sqlite");
        jdbi.registerArgumentFactory(new LocalDateArgumentFactory());
        RecordDao recordDao = jdbi.onDemand(RecordDao.class);
        recordDao.createTable();

        // TODO dependency injection
        CreateRecord createRecord = new CreateRecord(recordDao);
        GetRecords getRecords = new GetRecords(recordDao);
        UpdateRecord updateRecord = new UpdateRecord(recordDao);
        DeleteRecord deleteRecord = new DeleteRecord(recordDao);
        RecordResource recordResource = new RecordResource(createRecord, getRecords, deleteRecord, updateRecord);
        environment.jersey().register(recordResource);

        environment.jersey().register(new BuildResource());
    }

}
