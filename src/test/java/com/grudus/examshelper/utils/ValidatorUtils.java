package com.grudus.examshelper.utils;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;

public class ValidatorUtils {

    public static void assertErrorKeys(Errors errors, String... keys) {
        assertTrue(codes(errors).containsAll(asList(keys)));
    }

    private static List<String> codes(Errors errors) {
        return errors.getAllErrors().stream().map(DefaultMessageSourceResolvable::getCode).collect(Collectors.toList());
    }
}
