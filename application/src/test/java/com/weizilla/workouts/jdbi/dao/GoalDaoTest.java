package com.weizilla.workouts.jdbi.dao;

import com.codahale.metrics.MetricRegistry;
import com.weizilla.workouts.entity.Goal;
import com.weizilla.workouts.entity.ImmutableGoal;
import com.weizilla.workouts.entity.ObjectMappers;
import com.weizilla.workouts.entity.TestEntity;
import com.weizilla.workouts.entity.TimeOfDay;
import com.weizilla.workouts.jdbi.dbi.DbiFactory;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.setup.Environment;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.util.StringColumnMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.weizilla.workouts.entity.TestEntity.DATE;
import static com.weizilla.workouts.entity.TestEntity.DISTANCE;
import static com.weizilla.workouts.entity.TestEntity.DURATION;
import static com.weizilla.workouts.entity.TestEntity.GOAL_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class GoalDaoTest {
    private GoalDao dao;
    private DBI dbi;
    private Goal goal;

    @Before
    public void setUp() throws Exception {
        Environment environment = new Environment("test-env", ObjectMappers.OBJECT_MAPPER, null, new MetricRegistry(), null);
        DataSourceFactory dataSourceFactory = new DataSourceFactory();
        dataSourceFactory.setDriverClass("org.sqlite.JDBC");
        dataSourceFactory.setUrl("jdbc:sqlite::memory:");
        dbi = DbiFactory.createDbi(environment, dataSourceFactory);
        dao = dbi.onDemand(GoalDao.class);

        goal = TestEntity.createGoal();
    }

    @Test
    public void createsTable() throws Exception {
        dao.createTable();
        String tableName = dbi.withHandle(handle ->
            handle.createQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='goals'")
                .map(StringColumnMapper.INSTANCE)
                .first()
        );
        assertThat(tableName).isEqualTo("goals");
    }

    @Test
    public void addAndGetsAllGoals() throws Exception {
        dao.createTable();
        dao.add(goal);
        List<Goal> all = dao.getAll();
        assertThat(all).containsExactly(goal);
    }

    @Test
    public void addAndGetByUuid() throws Exception {
        dao.createTable();
        dao.add(goal);
        Goal actual = dao.get(GOAL_ID);
        assertThat(actual).isEqualTo(goal);
    }

    @Test
    public void addAndGetByUuidWithoutOptionals() throws Exception {
        Goal withoutOptionals = ImmutableGoal.copyOf(goal)
            .withDistance(Optional.empty())
            .withDuration(Optional.empty());

        dao.createTable();
        dao.add(withoutOptionals);
        Goal actual = dao.get(GOAL_ID);
        assertThat(actual).isEqualTo(withoutOptionals);
    }

    @Test
    public void addAndGetByDate() throws Exception {
        dao.createTable();
        dao.add(goal);
        List<Goal> all = dao.get(DATE);
        assertThat(all).containsExactly(goal);
    }

    @Test
    public void addAndOnlyGetForDate() throws Exception {
        dao.createTable();
        dao.add(goal);
        ImmutableGoal newDate = ImmutableGoal.copyOf(goal)
            .withId(UUID.randomUUID())
            .withDate(DATE.plusMonths(1));
        dao.add(newDate);
        List<Goal> all = dao.get(DATE);
        assertThat(all).containsExactly(this.goal);
    }

    @Test
    public void updatesGoal() throws Exception {
        dao.createTable();
        dao.add(goal);

        Goal updated = ImmutableGoal.copyOf(goal)
            .withType("NEW TYPE")
            .withDate(DATE.plusMonths(1))
            .withDuration(DURATION.plusHours(1))
            .withDistance(DISTANCE.multipliedBy(2))
            .withTimeOfDay(TimeOfDay.EVENING)
            .withNotes("NEW NOTE");
        dao.update(updated);

        Goal afterUpdate = dao.get(GOAL_ID);
        assertThat(afterUpdate).isEqualTo(updated);
        assertThat(afterUpdate).isNotEqualTo(goal);
    }

    @Test
    public void deletesGoal() throws Exception {
        dao.createTable();
        dao.add(goal);
        dao.delete(GOAL_ID);
        List<Goal> afterDelete = dao.getAll();
        assertThat(afterDelete).isEmpty();
    }

    @Test
    public void deletesOnlyGoalWithId() throws Exception {
        dao.createTable();
        dao.add(goal);

        UUID otherId = UUID.randomUUID();
        Goal otherGoal = ImmutableGoal.copyOf(goal)
            .withId(otherId);
        dao.add(otherGoal);

        dao.delete(GOAL_ID);

        List<Goal> afterDelete = dao.getAll();
        assertThat(afterDelete).containsOnly(otherGoal);
    }
}
