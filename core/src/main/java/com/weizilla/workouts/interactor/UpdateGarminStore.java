package com.weizilla.workouts.interactor;

import com.weizilla.garmin.downloader.ActivityDownloader;
import com.weizilla.workouts.entity.Activity;
import com.weizilla.workouts.entity.ImmutableActivity;
import com.weizilla.workouts.store.GarminStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Singleton
public class UpdateGarminStore {
    private static final Logger logger = LoggerFactory.getLogger(UpdateGarminStore.class);
    private final ActivityDownloader activityDownloader;
    private final GarminStore garminStore;
    private final ExecutorService executorService;

    @Inject
    public UpdateGarminStore(ActivityDownloader activityDownloader, GarminStore garminStore) {
        this.activityDownloader = activityDownloader;
        this.garminStore = garminStore;
        executorService = Executors.newSingleThreadExecutor();
    }

    public Future<?> startUpdate() throws Exception {
        //TODO track jobs
        return executorService.submit(() -> {
            try {
                List<Activity> downloaded = activityDownloader.download().stream()
                    .map(UpdateGarminStore::createActivity)
                    .collect(Collectors.toList());
                garminStore.addAll(downloaded);
                logger.info("Downloaded {} activities", downloaded.size());
            } catch (Exception e) {
                logger.error("Error download Garmin activities", e);
            }
        });
    }

    protected static Activity createActivity(com.weizilla.garmin.entity.Activity fromGarmin) {
        return ImmutableActivity.builder()
            .id(fromGarmin.getId())
            .type(fromGarmin.getType())
            .start(fromGarmin.getStart())
            .distance(fromGarmin.getDistance())
            .duration(fromGarmin.getDuration())
            .build();
    }
}
