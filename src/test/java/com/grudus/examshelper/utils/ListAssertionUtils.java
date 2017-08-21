package com.grudus.examshelper.utils;

import java.util.List;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class ListAssertionUtils {

    @SafeVarargs
    public static <T, R> void assertContainsProperties(List<T> items, Function<T, R> mapper, R... expected) {
        assertContainsProperties(items, mapper, asList(expected));
    }

    public static <T, R> void assertContainsProperties(List<T> items, Function<T, R> mapper, List<R> expected) {
        List<R> mapped = items.stream().map(mapper).collect(toList());
        assertThat(mapped, containsInAnyOrder(expected.toArray()));
    }
}
