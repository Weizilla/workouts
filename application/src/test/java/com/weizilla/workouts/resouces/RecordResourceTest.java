package com.weizilla.workouts.resouces;

import com.weizilla.workouts.entity.ImmutableRecord;
import com.weizilla.workouts.entity.ObjectMappers;
import com.weizilla.workouts.entity.Record;
import com.weizilla.workouts.interactor.CreateRecord;
import com.weizilla.workouts.interactor.DeleteRecord;
import com.weizilla.workouts.interactor.GetRecords;
import com.weizilla.workouts.interactor.UpdateRecord;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import tec.uom.se.quantity.Quantities;

import javax.measure.Quantity;
import javax.measure.quantity.Length;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static systems.uom.common.USCustomary.MILE;

@RunWith(MockitoJUnitRunner.class)
public class RecordResourceTest {
    protected static final UUID ID = UUID.randomUUID();
    protected static final String TYPE = "TYPE";
    protected static final LocalDate DATE = LocalDate.now();
    protected static final int RATING = 3;
    protected static final Duration DURATION = Duration.ofHours(1);
    protected static final Quantity<Length> DISTANCE = Quantities.getQuantity(BigDecimal.valueOf(1.0), MILE);
    protected static final String COMMENT = "COMMENT";
    private static final Record record = ImmutableRecord.builder()
        .id(ID)
        .type(TYPE)
        .date(DATE)
        .rating(RATING)
        .duration(DURATION)
        .distance(DISTANCE)
        .comment(COMMENT)
        .build();

    private static final CreateRecord createRecord = mock(CreateRecord.class);
    private static final GetRecords getRecords = mock(GetRecords.class);
    private static final UpdateRecord updateRecord = mock(UpdateRecord.class);
    private static final DeleteRecord deleteRecord = mock(DeleteRecord.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
        .addResource(new RecordResource(createRecord, getRecords, deleteRecord, updateRecord))
        .setMapper(ObjectMappers.OBJECT_MAPPER)
        .build();

    @After
    public void tearDown() throws Exception {
        reset(createRecord, getRecords, updateRecord, deleteRecord);
    }

    @Test
    public void getsAllRecords() throws Exception {
        when(getRecords.getAll()).thenReturn(singletonList(record));
        List<Record> actual = resources.target("/records").request().get(List.class);
        assertThat(actual).containsExactly(record);
    }
}