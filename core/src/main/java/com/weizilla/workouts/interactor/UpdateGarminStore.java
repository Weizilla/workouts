package com.weizilla.workouts.interactor;

import com.weizilla.garmin.downloader.ActivityDownloader;
import com.weizilla.garmin.entity.Activity;
import com.weizilla.workouts.store.GarminStore;

import java.util.List;

public class UpdateGarminStore {
    private final ActivityDownloader activityDownloader;
    private final GarminStore garminStore;

    public UpdateGarminStore(ActivityDownloader activityDownloader, GarminStore garminStore) {
        this.activityDownloader = activityDownloader;
        this.garminStore = garminStore;
    }

    public int update() throws Exception {
        List<Activity> downloaded = activityDownloader.download();
        garminStore.addAll(downloaded);
        return downloaded.size();
    }
}
