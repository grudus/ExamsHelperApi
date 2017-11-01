package com.grudus.examshelper.utils;

import com.grudus.examshelper.commons.Pair;

import static java.util.Objects.requireNonNull;

public class RequestParam extends Pair<String, String> {

    private RequestParam(String first, String second) {
        super(first, second);
    }

    public static RequestParam param(String first, Object second) {
        requireNonNull(first);
        requireNonNull(second);
        return new RequestParam(first, second.toString());
    }
}
