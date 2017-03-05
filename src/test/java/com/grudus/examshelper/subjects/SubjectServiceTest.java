package com.grudus.examshelper.subjects;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static com.grudus.examshelper.Utils.randomSubject;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SubjectServiceTest {

    @Mock
    private SubjectDao subjectDao;

    private SubjectService subjectService;

    private Subject subject = randomSubject(-1L);

    @Before
    public void init() {
        subjectService = new SubjectService(subjectDao);
    }

    @Test
    public void shouldSaveSubject() {
        subjectService.save(subject);

        verify(subjectDao).save(eq(subject));
    }

    @Test
    public void shouldUpdateSubject() {
        subjectService.update(subject);

        verify(subjectDao).update(eq(subject));
    }

    @Test
    public void shouldFindByUser() {
        when(subjectDao.findByUserId(anyLong())).thenReturn(singletonList(subject));

        List<Subject> subjectList = subjectService.findByUser(subject.getUserId());

        verify(subjectDao).findByUserId(eq(subject.getUserId()));
        assertThat(subjectList, hasItem(is(subject)));
    }

}
