package com.grudus.examshelper.commons;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;

import static com.grudus.examshelper.commons.Constant.DATE_PATTERN;
import static java.time.format.DateTimeFormatter.ofPattern;

public class JsonLocalDateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return LocalDate.parse(jsonParser.getText(), ofPattern(DATE_PATTERN));
    }
}
