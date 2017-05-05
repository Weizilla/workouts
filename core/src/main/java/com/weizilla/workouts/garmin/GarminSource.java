package com.weizilla.workouts.garmin;

import com.weizilla.garmin.entity.Activity;

import java.util.List;

public interface GarminSource {
    List<Activity> fetch();
}
