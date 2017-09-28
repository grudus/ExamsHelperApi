package com.grudus.examshelper;

import com.grudus.examshelper.emails.EmailSender;
import com.grudus.examshelper.tables.records.UsersRecord;
import com.grudus.examshelper.users.User;
import com.grudus.examshelper.users.roles.Role;
import com.grudus.examshelper.users.roles.RoleName;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.List;

import static com.grudus.examshelper.Tables.ROLES;
import static com.grudus.examshelper.Tables.USER_ROLES;
import static com.grudus.examshelper.tables.Users.USERS;
import static com.grudus.examshelper.utils.Utils.randAlph;
import static com.grudus.examshelper.utils.Utils.randomUser;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestContext.class)
@WebAppConfiguration
@Transactional
@Rollback
public abstract class SpringBasedTest {

    @Autowired
    protected DSLContext dsl;

    @Autowired
    private EmailSender emailSender;

    @BeforeEach
    public void cleanMocks() throws MessagingException {
        Mockito.reset(emailSender);
        doNothing().when(emailSender).send(anyString(), anyString());
    }


    protected User addUserWithRoles(RoleName... roles) {
        User currentUser = randomUser();
        currentUser.setToken(randAlph(33));

        UsersRecord record = dsl.newRecord(USERS, currentUser);
        record.insert();
        Long userId = record.getId();
        currentUser.setId(userId);

        List<Role> roleList =
                stream(roles)
                        .peek(role ->
                                dsl.insertInto(USER_ROLES, USER_ROLES.USER_ID, USER_ROLES.ROLE_ID)
                                        .values(userId, dsl.selectFrom(ROLES).where(ROLES.NAME.eq(role.toString())).fetchOne(ROLES.ID))
                                        .execute()
                        ).map(Role::new)
                        .collect(toList());

        currentUser.setRoles(roleList);
        return currentUser;
    }
}
