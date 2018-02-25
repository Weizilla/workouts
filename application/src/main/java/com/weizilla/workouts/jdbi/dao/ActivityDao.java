package com.weizilla.workouts.jdbi.dao;

import com.weizilla.workouts.entity.Activity;
import com.weizilla.workouts.store.GarminStore;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RegisterMapper(ActivityMapper.class)
public interface ActivityDao extends GarminStore {

    String INSERT_SQL = "INSERT OR REPLACE INTO activities (id, type, start_time, date, duration, distance, created_time) " +
        "VALUES (:id, :type, :startTime, :date, :duration, :distance, :createdTime)";

    @SqlUpdate("CREATE TABLE IF NOT EXISTS activities (" +
        "id INTEGER PRIMARY KEY, " +
        "type TEXT NOT NULL, " +
        "start_time TEXT NOT NULL, " +
        "date TEXT NOT NULL, " +
        "duration INTEGER NOT NULL, " +
        "distance INTEGER NOT NULL, " +
        "created_time INTEGER NOT NULL " +
        ")")
    void createTable();

    @Override
    @SqlUpdate(INSERT_SQL)
    void add(@BindBean Activity activity);

    @SqlBatch(INSERT_SQL)
    void addAll(@BindBean Collection<Activity> activities);

    @Override
    @SqlQuery("SELECT * FROM activities WHERE date = :date")
    List<Activity> get(@Bind("date") LocalDate date);

    @Override
    @SqlQuery("SELECT * FROM activities WHERE id = :id")
    Activity get(@Bind("id") Long id);

    @Override
    @SqlQuery("SELECT * FROM activities")
    List<Activity> getAll();
}
