package com.grudus.examshelper.stats;

public class AverageExamsGradePerMonth {
    private final Month month;
    private final Double averageGrade;
    private final Integer year;

    public AverageExamsGradePerMonth(Integer year, Month month, Double averageGrade) {
        this.month = month;
        this.averageGrade = averageGrade;
        this.year = year;
    }

    public Month getMonth() {
        return month;
    }

    public Double getAverageGrade() {
        return averageGrade;
    }

    public Integer getYear() {
        return year;
    }
}
