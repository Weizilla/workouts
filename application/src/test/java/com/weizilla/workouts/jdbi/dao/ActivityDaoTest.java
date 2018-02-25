package com.weizilla.workouts.jdbi.dao;

import com.codahale.metrics.MetricRegistry;
import com.weizilla.workouts.entity.Activity;
import com.weizilla.workouts.entity.ImmutableActivity;
import com.weizilla.workouts.entity.ObjectMappers;
import com.weizilla.workouts.entity.TestEntity;
import com.weizilla.workouts.jdbi.dbi.DbiFactory;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.setup.Environment;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.util.StringColumnMapper;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.weizilla.workouts.entity.TestEntity.ACTIVITY_ID;
import static com.weizilla.workouts.entity.TestEntity.DURATION;
import static com.weizilla.workouts.entity.TestEntity.START;
import static org.assertj.core.api.Assertions.assertThat;

public class ActivityDaoTest {
    private ActivityDao dao;
    private DBI dbi;
    private Activity activity;

    @Before
    public void setUp() throws Exception {
        Environment environment = new Environment("test-env", ObjectMappers.OBJECT_MAPPER, null, new MetricRegistry(), null);
        DataSourceFactory dataSourceFactory = new DataSourceFactory();
        dataSourceFactory.setDriverClass("org.sqlite.JDBC");
        dataSourceFactory.setUrl("jdbc:sqlite::memory:");
        dbi = DbiFactory.createDbi(environment, dataSourceFactory);
        dao = dbi.onDemand(ActivityDao.class);

        activity = TestEntity.createActivity();
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
        Activity actual = dao.get(ACTIVITY_ID);
        assertThat(actual).isEqualTo(activity);
    }

    @Test
    public void updateOnDuplicateAdd() throws Exception {
        dao.createTable();
        dao.add(activity);

        Activity actual = dao.get(ACTIVITY_ID);
        assertThat(actual).isEqualTo(activity);

        Duration duration1 = DURATION.plusHours(1);
        Activity update1 = ImmutableActivity.copyOf(activity)
            .withDuration(duration1);
        dao.add(update1);

        actual = dao.get(ACTIVITY_ID);
        assertThat(actual).isEqualTo(update1);

        Duration duration2 = DURATION.plusHours(2);
        Activity update2 = ImmutableActivity.copyOf(activity)
            .withDuration(duration2);
        dao.add(update2);

        actual = dao.get(ACTIVITY_ID);
        assertThat(actual).isEqualTo(update2);
    }

    @Test
    public void addAllAndGetById() throws Exception {
        int num = 3;
        List<Activity> activities = IntStream.range(0, num)
            .mapToObj(i -> ImmutableActivity.copyOf(activity).withId(ACTIVITY_ID + i))
            .collect(Collectors.toList());

        dao.createTable();
        dao.addAll(activities);

        for (int i = 0; i < num; i++) {
            Activity actual = dao.get(ACTIVITY_ID + i);
            assertThat(actual).isEqualTo(activities.get(i));
        }
    }

    @Test
    public void updateAllOnDuplicateAdd() throws Exception {
        dao.createTable();
        dao.addAll(Collections.singletonList(activity));

        Activity actual = dao.get(ACTIVITY_ID);
        assertThat(actual).isEqualTo(activity);

        Duration duration1 = DURATION.plusHours(1);
        Activity update1 = ImmutableActivity.copyOf(activity)
            .withDuration(duration1);
        dao.addAll(Collections.singletonList(update1));

        actual = dao.get(ACTIVITY_ID);
        assertThat(actual).isEqualTo(update1);

        Duration duration2 = DURATION.plusHours(2);
        Activity update2 = ImmutableActivity.copyOf(activity)
            .withDuration(duration2);
        dao.addAll(Collections.singletonList(update2));

        actual = dao.get(ACTIVITY_ID);
        assertThat(actual).isEqualTo(update2);
    }

    @Test
    public void addAllAndGetAll() throws Exception {
        int num = 3;
        List<Activity> activities = IntStream.range(0, num)
            .mapToObj(i -> ImmutableActivity.copyOf(activity).withId(ACTIVITY_ID + i))
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
