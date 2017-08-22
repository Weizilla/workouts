package com.weizilla.workouts.entity;

import org.junit.Test;
import tec.uom.se.quantity.Quantities;

import javax.measure.Quantity;
import javax.measure.quantity.Length;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static systems.uom.common.USCustomary.MILE;

public class ObjectMappersTest {
    protected static final UUID ID = UUID.randomUUID();
    protected static final String TYPE = "TYPE";
    protected static final boolean OUTDOOR = true;
    protected static final LocalDate DATE = LocalDate.now();
    protected static final int RATING = 3;
    protected static final Duration DURATION = Duration.ofHours(1);
    protected static final Quantity<Length> DISTANCE = Quantities.getQuantity(BigDecimal.valueOf(1.0), MILE);
    protected static final String COMMENT = "COMMENT";
    private static Record record = ImmutableRecord.builder()
        .id(ID)
        .type(TYPE)
        .isOutdoor(OUTDOOR)
        .date(DATE)
        .rating(RATING)
        .duration(DURATION)
        .distance(DISTANCE)
        .comment(COMMENT)
        .build();

    @Test
    public void serializesRecord() throws Exception {
        String actual = ObjectMappers.OBJECT_MAPPER.writeValueAsString(record);
        assertThat(actual).isNotEmpty();
    }

    @Test
    public void roundTrip() throws Exception {
        String json = ObjectMappers.OBJECT_MAPPER.writeValueAsString(record);
        assertThat(json).isNotEmpty();

        Record pojo = ObjectMappers.OBJECT_MAPPER.readValue(json, Record.class);
        assertThat(pojo).isEqualTo(record);
    }
}