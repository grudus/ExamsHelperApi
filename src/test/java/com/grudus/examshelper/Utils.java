package com.grudus.examshelper;


import com.grudus.examshelper.exams.Exam;
import com.grudus.examshelper.subjects.Subject;
import com.grudus.examshelper.users.User;
import com.grudus.examshelper.users.auth.AddUserRequest;
import org.junit.Ignore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static java.util.stream.IntStream.range;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@Ignore
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
        return '#' + range(0, 6).mapToObj(i -> Integer.toHexString(RANDOM.nextInt(16)))
                .collect(Collectors.joining(""));
    }

    public static Subject randomSubject(Long userId) {
        return new Subject(userId, randAlph(12), randomColor());
    }

    public static long randomBetween(long a, long b) {
        return a + (long) (Math.random() * (b - a));
    }

    public static LocalDateTime randomDate(LocalDateTime from, LocalDateTime to) {
        LocalDate dateFrom = from.toLocalDate(), dateTo = to.toLocalDate();
        LocalTime timeFrom = from.toLocalTime(), timeTo = to.toLocalTime();

        LocalDate randomDate = LocalDate.ofEpochDay(randomBetween(dateFrom.toEpochDay(), dateTo.toEpochDay()));
        LocalTime randomTime = LocalTime.ofSecondOfDay(randomBetween(timeFrom.toSecondOfDay(), timeTo.toSecondOfDay()));
        return LocalDateTime.of(randomDate, randomTime);
    }

    public static Exam randomExam(Long subjectId) {
        return new Exam(randAlph(12), randomDate(now(), now().plusYears(1)), (Math.random() * 6), subjectId);
    }
}
