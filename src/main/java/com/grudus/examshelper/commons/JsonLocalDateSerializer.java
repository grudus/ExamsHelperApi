package com.grudus.examshelper.commons;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;

import static com.grudus.examshelper.commons.Constant.DATE_PATTERN;
import static java.time.format.DateTimeFormatter.ofPattern;

public class JsonLocalDateSerializer extends JsonSerializer<LocalDate> {

    @Override
    public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(localDate.format(ofPattern(DATE_PATTERN)));
    }
}
