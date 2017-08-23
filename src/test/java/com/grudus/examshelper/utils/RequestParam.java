package com.grudus.examshelper.utils;

import com.grudus.examshelper.commons.Pair;

public class RequestParam extends Pair<String, String> {

    private RequestParam(String first, String second) {
        super(first, second);
    }

    public static RequestParam param(String first, String second) {
        return new RequestParam(first, second);
    }
}
