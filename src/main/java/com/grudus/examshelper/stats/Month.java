package com.grudus.examshelper.stats;

import java.time.LocalDateTime;
import java.util.Objects;

public class Month implements Comparable<Month> {
    private final int index;
    private final Name name;

    /**
     * @param index natural-order based index (e.g. february is 2th)
     */
    public Month(int index) {
        assertIndex(index);
        this.index = index;
        this.name = Name.values()[index - 1];
    }

    public Month(Name name) {
        Objects.requireNonNull(name);
        this.name = name;
        this.index = name.ordinal() + 1;
    }

    public Month(LocalDateTime localDateTime) {
        this(localDateTime.getMonthValue());
    }

    public int getIndex() {
        return index;
    }

    public Name getName() {
        return name;
    }

    private void assertIndex(int index) {
        if (index < 1 || index > 12)
            throw new IllegalArgumentException("Month must be within [1-12] range");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Month month = (Month) o;

        return index == month.index;
    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public int compareTo(Month o) {
        return this.index - o.index;
    }


    public enum Name {
        JANUARY,
        FEBRUARY,
        MARCH,
        APRIL,
        MAY,
        JUNE,
        JULY,
        AUGUST,
        SEPTEMBER,
        OCTOBER,
        NOVEMBER,
        DECEMBER
    }
}
