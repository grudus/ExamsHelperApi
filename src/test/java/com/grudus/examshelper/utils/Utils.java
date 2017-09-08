package com.grudus.examshelper.utils;


import com.grudus.examshelper.subjects.Subject;
import com.grudus.examshelper.users.User;
import com.grudus.examshelper.users.auth.AddUserRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

import static java.lang.Integer.toHexString;
import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.range;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class Utils {
    private static final Random RANDOM = new Random();

    public static String randAlph(int length) {
        return randomAlphabetic(length);
    }

    public static String randomEmail() {
        return randAlph(12) + "@" + randAlph(12) + ".com";
    }

    public static User randomUser() {
        return new User(randAlph(12), randomAlphabetic(12), randomEmail());
    }

    public static AddUserRequest randomAddUserRequest() {
        return new AddUserRequest(randAlph(12), randAlph(12), randomEmail());
    }

    public static String randomColor() {
        return '#' + range(0, 6).mapToObj(i -> toHexString(RANDOM.nextInt(16)))
                .collect(joining(""));
    }

    public static Subject randomSubject(Long userId) {
        return new Subject(userId, randAlph(12), randomColor());
    }

    private static long randomBetween(long a, long b) {
        return a + (long) (Math.random() * (b - a));
    }

    static LocalDateTime randomDate(LocalDateTime from, LocalDateTime to) {
        LocalDate dateFrom = from.toLocalDate(), dateTo = to.toLocalDate();
        LocalTime timeFrom = from.toLocalTime(), timeTo = to.toLocalTime();

        LocalDate randomDate = LocalDate.ofEpochDay(randomBetween(dateFrom.toEpochDay(), dateTo.toEpochDay()));
        LocalTime randomTime = LocalTime.ofSecondOfDay(randomBetween(timeFrom.toSecondOfDay(), timeTo.toSecondOfDay()));
        return LocalDateTime.of(randomDate, randomTime);
    }

}
