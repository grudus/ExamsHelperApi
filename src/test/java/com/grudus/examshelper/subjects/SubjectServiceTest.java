package com.grudus.examshelper.subjects;

import com.grudus.examshelper.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import java.util.List;

import static com.grudus.examshelper.utils.Utils.randomSubject;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock
    private SubjectDao subjectDao;

    private SubjectService subjectService;

    private Subject subject = randomSubject(-1L);

    @BeforeEach
    void init() {
        subjectService = new SubjectService(subjectDao);
    }

    @Test
    void shouldSaveSubject() {
        subjectService.save(subject);

        verify(subjectDao).save(eq(subject));
    }

    @Test
    void shouldUpdateSubject() {
        subjectService.update(subject);

        verify(subjectDao).update(eq(subject));
    }

    @Test
    void shouldFindByUser() {
        when(subjectDao.findByUserId(anyLong())).thenReturn(singletonList(subject));

        List<Subject> subjectList = subjectService.findByUser(subject.getUserId());

        verify(subjectDao).findByUserId(eq(subject.getUserId()));
        assertThat(subjectList, hasItem(is(subject)));
    }

    @Test
    void shouldDeleteSubjectById() {
        Long id = 414L;
        subjectService.delete(id);

        verify(subjectDao).delete(eq(id));
    }

}
