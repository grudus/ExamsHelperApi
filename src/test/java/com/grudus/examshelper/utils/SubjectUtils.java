package com.grudus.examshelper.utils;

import com.grudus.examshelper.subjects.SubjectDto;

import static com.grudus.examshelper.utils.Utils.randAlph;
import static com.grudus.examshelper.utils.Utils.randomColor;

public class SubjectUtils {

    public static SubjectDto randomSubjectDto() {
        return randomSubjectDto(null);
    }

    public static SubjectDto randomSubjectDto(Long id) {
        return new SubjectDto(id, randAlph(11), randomColor());
    }
}
