package com.grudus.examshelper;


import com.grudus.examshelper.users.User;
import org.junit.Ignore;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@Ignore
public class Utils {
    public static String randAlph(int length) {
        return randomAlphabetic(length);
    }

    public static String randomEmail() {
        return randAlph(5) + "@" + randAlph(5) + ".com";
    }

    public static User randomUser() {
        return new User(randAlph(12), randomAlphabetic(12), randomEmail());
    }
}
