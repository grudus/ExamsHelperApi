package com.grudus.examshelper.exams.domain;

public class NotGradedExamsCount {
    private final Integer count;

    public NotGradedExamsCount(Integer count) {
        assertPositiveNumber(count);
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    private void assertPositiveNumber(Integer count) {
        if (count < 0)
            throw new IllegalArgumentException("Count cannot be a negative number!");
    }
}
