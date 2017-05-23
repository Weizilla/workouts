package com.weizilla.workouts.interactor;

import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.store.RecordStore;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public abstract class RecordTest {
    protected static final UUID ID = UUID.randomUUID();
    protected static final String TYPE = "TYPE";
    protected static final LocalDate DATE = LocalDate.now();
    protected static final int RATING = 3;
    protected static final Duration DURATION = Duration.ofHours(1);
    protected static final String COMMENT = "COMMENT";
    protected Record record;
    @Mock
    protected RecordStore recordStore;

    @Before
    public void setUp() throws Exception {
        record  = new Record(ID, TYPE, DATE, RATING);
    }
}