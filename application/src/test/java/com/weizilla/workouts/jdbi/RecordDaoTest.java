package com.weizilla.workouts.jdbi;

import com.codahale.metrics.MetricRegistry;
import com.weizilla.distance.Distance;
import com.weizilla.workouts.entity.ImmutableRecord;
import com.weizilla.workouts.entity.ObjectMappers;
import com.weizilla.workouts.entity.Record;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.util.StringColumnMapper;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class RecordDaoTest {
    protected static final UUID ID = UUID.randomUUID();
    protected static final String TYPE = "TYPE";
    protected static final boolean OUTDOOR = true;
    protected static final LocalDate DATE = LocalDate.now();
    protected static final int RATING = 3;
    protected static final Duration DURATION = Duration.ofHours(1);
    protected static final Distance DISTANCE = Distance.ofMiles(1);
    protected static final String COMMENT = "COMMENT";
    private RecordDao dao;
    private DBI dbi;
    private Record record;

    @Before
    public void setUp() throws Exception {
        Environment environment = new Environment("test-env", ObjectMappers.OBJECT_MAPPER, null, new MetricRegistry(), null);
        DataSourceFactory dataSourceFactory = new DataSourceFactory();
        dataSourceFactory.setDriverClass("org.sqlite.JDBC");
        dataSourceFactory.setUrl("jdbc:sqlite::memory:");
        dbi = new DBIFactory().build(environment, dataSourceFactory, "sqlite");
        dbi.registerArgumentFactory(new LocalDateArgumentFactory());
        dao = dbi.onDemand(RecordDao.class);

        record = ImmutableRecord.builder()
            .id(ID)
            .type(TYPE)
            .outdoor(OUTDOOR)
            .date(DATE)
            .rating(RATING)
            .duration(DURATION)
            .distance(DISTANCE)
            .comment(COMMENT)
            .build();
    }

    @Test
    public void createsTable() throws Exception {
        dao.createTable();
        String tableName = dbi.withHandle(handle ->
            handle.createQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='records'")
                .map(StringColumnMapper.INSTANCE)
                .first()
        );
        assertThat(tableName).isEqualTo("records");
    }

    @Test
    public void addAndGetsAllRecords() throws Exception {
        dao.createTable();
        dao.add(record);
        List<Record> all = dao.getAll();
        assertThat(all).containsExactly(record);
    }

    @Test
    public void addAndGetByUuid() throws Exception {
        dao.createTable();
        dao.add(record);
        Record actual = dao.get(ID);
        assertThat(actual).isEqualTo(record);
    }

    @Test
    public void addAndGetByDate() throws Exception {
        dao.createTable();
        dao.add(record);
        List<Record> all = dao.get(DATE);
        assertThat(all).containsExactly(record);
    }

    @Test
    public void addAndOnlyGetForDate() throws Exception {
        dao.createTable();
        dao.add(record);
        ImmutableRecord newDate = ImmutableRecord.copyOf(record)
            .withId(UUID.randomUUID())
            .withDate(DATE.plusMonths(1));
        dao.add(newDate);
        List<Record> all = dao.get(DATE);
        assertThat(all).containsExactly(this.record);
    }

    @Test
    public void updatesRecord() throws Exception {
        dao.createTable();
        dao.add(record);

        Record updated = ImmutableRecord.copyOf(record)
            .withType("NEW TYPE")
            .withOutdoor(false)
            .withDate(DATE.plusMonths(1))
            .withDuration(DURATION.plusHours(1))
            .withDistance(DISTANCE.multipliedBy(2))
            .withRating(RATING + 1)
            .withComment("NEW COMMENT");
        dao.update(updated);

        Record afterUpdate = dao.get(ID);
        assertThat(afterUpdate).isEqualTo(updated);
        assertThat(afterUpdate).isNotEqualTo(record);
    }

    @Test
    public void deletesRecord() throws Exception {
        dao.createTable();
        dao.add(record);
        dao.delete(ID);
        List<Record> afterDelete = dao.getAll();
        assertThat(afterDelete).isEmpty();
    }

    @Test
    public void deletesOnlyRecordWithId() throws Exception {
        dao.createTable();
        dao.add(record);

        UUID otherId = UUID.randomUUID();
        Record otherRecord = ImmutableRecord.copyOf(record)
            .withId(otherId);
        dao.add(otherRecord);

        dao.delete(ID);

        List<Record> afterDelete = dao.getAll();
        assertThat(afterDelete).containsOnly(otherRecord);
    }
}