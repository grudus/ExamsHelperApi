package com.grudus.examshelper.utils;

import com.grudus.examshelper.subjects.SubjectDto;

import static com.grudus.examshelper.utils.Utils.randAlph;
import static com.grudus.examshelper.utils.Utils.randomColor;

public class SubjectUtils {

    public static SubjectDto randomSubjectDto() {
        return new SubjectDto(null, randAlph(11), randomColor());
    }
}
