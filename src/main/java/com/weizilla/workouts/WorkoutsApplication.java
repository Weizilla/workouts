package com.weizilla.workouts;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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
        // TODO: application initialization
    }

    @Override
    public void run(final WorkoutsConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
