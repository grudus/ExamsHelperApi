package com.grudus.examshelper;

import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.grudus.examshelper.Tables.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestContext.class)
@Ignore
public class DaoTest {

    @Autowired
    protected DSLContext dsl;

    @Before
    public void cleanDb() {
        dsl.deleteFrom(USERS).execute();
        dsl.deleteFrom(ROLES).execute();
        dsl.deleteFrom(USER_ROLES).execute();
        dsl.deleteFrom(SUBJECTS).execute();
        dsl.deleteFrom(EXAMS).execute();
    }
}
