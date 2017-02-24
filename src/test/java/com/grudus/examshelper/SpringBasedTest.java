package com.grudus.examshelper;

import com.grudus.examshelper.emails.EmailSender;
import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestContext.class)
@WebAppConfiguration
@Transactional
@Rollback
public abstract class SpringBasedTest {

    @Autowired
    protected DSLContext dsl;

    @Autowired
    private EmailSender emailSender;

    @Before
    public void cleanMocks() {
        Mockito.reset(emailSender);
    }
}
