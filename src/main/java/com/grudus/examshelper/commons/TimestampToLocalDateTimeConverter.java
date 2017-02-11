package com.grudus.examshelper.commons;

import org.jooq.Converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TimestampToLocalDateTimeConverter implements Converter<Timestamp, LocalDateTime> {

    @Override
    public LocalDateTime from(Timestamp databaseObject) {
        return (databaseObject != null) ? databaseObject.toLocalDateTime() : null;
    }

    @Override
    public Timestamp to(LocalDateTime userObject) {
        return Timestamp.valueOf(userObject);
    }

    @Override
    public Class<Timestamp> fromType() {
        return Timestamp.class;
    }

    @Override
    public Class<LocalDateTime> toType() {
        return LocalDateTime.class;
    }
}