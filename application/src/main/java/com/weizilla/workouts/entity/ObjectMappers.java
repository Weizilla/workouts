package com.weizilla.workouts.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.weizilla.distance.Distance;
import io.dropwizard.jackson.Jackson;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ObjectMappers {
    public static final ObjectMapper OBJECT_MAPPER = create();

    public static ObjectMapper create() {
        ObjectMapper objectMapper = Jackson.newMinimalObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(LocalDate.class, new LocalDateSerializer());
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        module.addSerializer(Duration.class, new DurationSerializer());
        module.addSerializer(Instant.class, new InstantSerializer());
        objectMapper.registerModule(module);

        SimpleModule quantityModule = new SimpleModule();
        quantityModule.addSerializer(Distance.class, new DistanceSerializer());
        quantityModule.addDeserializer(Distance.class, new DistanceDeserializer());
        objectMapper.registerModule(quantityModule);

        objectMapper.registerModule(new Jdk8Module());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    private static class LocalDateSerializer extends JsonSerializer<LocalDate> {
        @Override
        public void serialize(LocalDate localDate, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            jsonGenerator.writeString(localDate.toString());
        }
    }

    private static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            jsonGenerator.writeString(localDateTime.toString());
        }
    }

    private static class DurationSerializer extends JsonSerializer<Duration> {
        @Override
        public void serialize(Duration duration, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            jsonGenerator.writeNumber(duration.getSeconds());
        }
    }

    private static class InstantSerializer extends JsonSerializer<Instant> {
        @Override
        public void serialize(Instant instant, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            jsonGenerator.writeNumber(instant.getEpochSecond());
        }
    }

    private static class DistanceSerializer extends JsonSerializer<Distance> {
        @Override
        public void serialize(Distance distance, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            jsonGenerator.writeNumber(distance.getDistanceMeter());
        }
    }

    private static class DistanceDeserializer extends JsonDeserializer<Distance> {
        @Override
        public Distance deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException, JsonProcessingException {
            return Distance.ofMeters(jsonParser.getLongValue());
        }
    }
}
