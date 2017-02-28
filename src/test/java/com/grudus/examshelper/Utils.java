package com.grudus.examshelper;


import com.grudus.examshelper.subjects.Subject;
import com.grudus.examshelper.users.User;
import com.grudus.examshelper.users.auth.AddUserRequest;
import org.junit.Ignore;

import java.util.Random;
import java.util.stream.Collectors;

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
}
