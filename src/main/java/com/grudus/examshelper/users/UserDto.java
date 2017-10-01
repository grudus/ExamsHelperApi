package com.grudus.examshelper.users;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.grudus.examshelper.commons.json.JsonLocalDateTimeDeserializer;
import com.grudus.examshelper.commons.json.JsonLocalDateTimeSerializer;

import java.time.LocalDateTime;

public class UserDto {
    public final Long id;
    public final String email;
    public final String username;
    @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
    @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
    public final LocalDateTime registerDate;

    public UserDto(Long id, String email, String username, LocalDateTime registerDate) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.registerDate = registerDate;
    }

}
