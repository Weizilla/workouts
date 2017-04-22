package com.weizilla.workouts.jdbi;

import com.weizilla.workouts.entity.Record;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(RecordMapper.class)
public interface RecordDao {
    @SqlUpdate("CREATE TABLE IF NOT EXISTS records (" +
        "id TEXT PRIMARY KEY, " +
        "type TEXT NOT NULL, " +
        "date TEXT NOT NULL, " +
        "duration TEXT, " +
        "rating INTEGER, " +
        "comment TEXT " +
        ")")
    void createTable();

    @SqlUpdate("INSERT INTO records (id, type, date, duration, rating, comment) " +
        "VALUES (:id, :type, :date, :duration, :rating, :comment)")
    void insert(@BindBean Record record);

    @SqlQuery("SELECT * FROM records")
    List<Record> getAll();
}
