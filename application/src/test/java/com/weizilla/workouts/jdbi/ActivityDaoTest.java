package com.weizilla.workouts.jdbi;

import com.codahale.metrics.MetricRegistry;
import com.weizilla.garmin.entity.Activity;
import com.weizilla.garmin.entity.ImmutableActivity;
import com.weizilla.workouts.entity.ObjectMappers;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.util.StringColumnMapper;
import tec.uom.se.quantity.Quantities;

import javax.measure.Quantity;
import javax.measure.quantity.Length;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static systems.uom.common.USCustomary.MILE;

public class ActivityDaoTest {
    protected static final long ID = 100;
    protected static final String TYPE = "TYPE";
    protected static LocalDateTime START = LocalDateTime.now();
    protected static final Duration DURATION = Duration.ofHours(1);
    protected static final Quantity<Length> DISTANCE =
        Quantities.getQuantity(BigDecimal.valueOf(1.0), MILE);
    private ActivityDao dao;
    private DBI dbi;
    private Activity activity;

    @Before
    public void setUp() throws Exception {
        Environment environment =
            new Environment("test-env", ObjectMappers.OBJECT_MAPPER, null, new MetricRegistry(), null);
        DataSourceFactory dataSourceFactory = new DataSourceFactory();
        dataSourceFactory.setDriverClass("org.sqlite.JDBC");
        dataSourceFactory.setUrl("jdbc:sqlite::memory:");
        dbi = new DBIFactory().build(environment, dataSourceFactory, "sqlite");
        dbi.registerArgumentFactory(new LocalDateArgumentFactory());
        dbi.registerArgumentFactory(new LocalDateTimeArgumentFactory());
        dao = dbi.onDemand(ActivityDao.class);

        activity = ImmutableActivity.builder()
            .id(ID)
            .type(TYPE)
            .start(START)
            .duration(DURATION)
            .distance(DISTANCE)
            .build();
    }

    @Test
    public void createsTable() throws Exception {
        dao.createTable();
        String tableName = dbi.withHandle(handle ->
            handle.createQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='activities'")
                .map(StringColumnMapper.INSTANCE)
                .first()
        );
        assertThat(tableName).isEqualTo("activities");
    }

    @Test
    public void addAndGetById() throws Exception {
        dao.createTable();
        dao.add(activity);
        Activity actual = dao.get(ID);
        assertThat(actual).isEqualTo(activity);
    }

    @Test
    public void addAllAndGetById() throws Exception {
        int num = 3;
        List<Activity> activities = IntStream.range(0, num)
            .mapToObj(i -> ImmutableActivity.copyOf(activity).withId(ID + i))
            .collect(Collectors.toList());

        dao.createTable();
        dao.addAll(activities);

        for (int i = 0; i < num; i++) {
            Activity actual = dao.get(ID + i);
            assertThat(actual).isEqualTo(activities.get(i));
        }
    }

    @Test
    public void addAllAndGetAll() throws Exception {
        int num = 3;
        List<Activity> activities = IntStream.range(0, num)
            .mapToObj(i -> ImmutableActivity.copyOf(activity).withId(ID + i))
            .collect(Collectors.toList());

        dao.createTable();
        dao.addAll(activities);

        List<Activity> actual = dao.getAll();
        assertThat(actual).isEqualTo(activities);
    }

    @Test
    public void getByDate() throws Exception {
        dao.createTable();
        dao.add(activity);

        List<Activity> actual = dao.get(START.toLocalDate());
        assertThat(actual).containsExactly(activity);
    }
}