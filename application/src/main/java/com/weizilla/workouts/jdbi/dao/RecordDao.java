package com.weizilla.workouts.jdbi.dao;

import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.store.RecordStore;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RegisterMapper(RecordMapper.class)
public interface RecordDao extends RecordStore {
    @SqlUpdate("CREATE TABLE IF NOT EXISTS records (" +
        "id TEXT PRIMARY KEY, " +
        "type TEXT NOT NULL, " +
        "outdoor INTEGER NOT NULL, " +
        "date TEXT NOT NULL, " +
        "duration INTEGER, " +
        "distance INTEGER, " +
        "rating INTEGER NOT NULL, " +
        "comment TEXT NOT NULL, " +
        "created_time INTEGER NOT NULL " +
        ")")
    void createTable();

    @Override
    @SqlQuery("SELECT * FROM records")
    List<Record> getAll();

    @Override
    @SqlUpdate("INSERT INTO records (id, type, outdoor, date, duration, distance, rating, comment, created_time) " +
        "VALUES (:id, :type, :outdoor, :date, :duration, :distance, :rating, :comment, :createdTime)")
    void add(@BindBean Record record);

    @Override
    @SqlQuery("SELECT * FROM records WHERE id = :id")
    Record get(@Bind("id") UUID id);

    @Override
    @SqlQuery("SELECT * FROM records WHERE date = :date")
    List<Record> get(@Bind("date") LocalDate date);

    @Override
    @SqlUpdate("UPDATE records SET " +
        "type = :type," +
        "outdoor = :outdoor," +
        "date = :date," +
        "duration = :duration," +
        "distance = :distance," +
        "rating = :rating," +
        "comment = :comment, " +
        "created_time = :createdTime " +
        "WHERE id = :id")
    void update(@BindBean Record record);

    @Override
    @SqlUpdate("DELETE FROM records WHERE id = :id")
    void delete(@Bind("id") UUID id);
}
