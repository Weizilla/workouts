package com.weizilla.workouts.jdbi;

import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.store.GoalStore;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RegisterMapper(GoalMapper.class)
public interface GoalDao extends GoalStore {
    @SqlUpdate("CREATE TABLE IF NOT EXISTS goals (" +
        "id STRING PRIMARY KEY, " +
        "type TEXT NOT NULL, " +
        "time_of_day TEXT NOT NULL, " +
        "date TEXT NOT NULL, " +
        "notes TEXT NOT NULL, " +
        "duration INTEGER, " +
        "distance INTEGER, " +
        "created_time INTEGER NOT NULL " +
        ")")
    void createTable();

    @Override
    @SqlUpdate("INSERT INTO goals (id, type, time_of_day, date, notes, duration, distance, created_time) " +
        "VALUES (:id, :type, :timeOfDay, :date, :notes, :duration, :distance, :createdTime)")
    void add(@BindBean Goal activity);

    @Override
    @SqlBatch("INSERT INTO goals (id, type, time_of_day, date, notes, duration, distance, created_time) " +
        "VALUES (:id, :type, :timeOfDay, :date, :notes, :duration, :distance, :createdTime)")
    void addAll(@BindBean Collection<Goal> activities);

    @Override
    @SqlQuery("SELECT * FROM goals WHERE date = :date")
    List<Goal> get(@Bind("date") LocalDate date);

    @Override
    @SqlQuery("SELECT * FROM goals WHERE id = :id")
    Goal get(@Bind("id") UUID id);

    @Override
    @SqlQuery("SELECT * FROM goals")
    List<Goal> getAll();

    @Override
    @SqlUpdate("UPDATE goals SET " +
        "type = :type," +
        "time_of_day = :timeOfDay," +
        "date = :date," +
        "duration = :duration," +
        "distance = :distance," +
        "notes = :notes, " +
        "created_time = :createdTime " +
        "WHERE id = :id")
    void update(@BindBean Goal goal);

    @Override
    @SqlUpdate("DELETE FROM goals WHERE id = :id")
    void delete(@Bind("id") UUID id);
}
