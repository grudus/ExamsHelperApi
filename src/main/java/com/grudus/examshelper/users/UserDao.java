package com.grudus.examshelper.users;


import com.grudus.examshelper.tables.Roles;
import com.grudus.examshelper.tables.UserRoles;
import com.grudus.examshelper.tables.Users;
import com.grudus.examshelper.tables.records.UsersRecord;
import com.grudus.examshelper.users.roles.Role;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@Repository
public class UserDao {

    public static final Users U = Users.USERS;
    public static final Roles R = Roles.ROLES;
    public static final UserRoles UR = UserRoles.USER_ROLES;

    private final DSLContext dsl;

    @Autowired
    public UserDao(DSLContext dsl) {
        this.dsl = dsl;
    }

    Optional<User> findByUsername(String username) {
        return dsl.selectFrom(U)
                .where(U.USERNAME.eq(username))
                .fetchOptionalInto(User.class);
    }

    Optional<User> findByToken(String token) {
        return dsl.selectFrom(U)
                .where(U.TOKEN.eq(token))
                .fetchOptionalInto(User.class);
    }

    Optional<User> findById(Long id) {
        return dsl.selectFrom(U)
                .where(U.ID.eq(id))
                .fetchOptionalInto(User.class);
    }

    void fetchUserPermissions(User u) {
        List<Role> roles = dsl.select(R.ID, R.NAME)
                .from(R).innerJoin(UR).onKey()
                .where(UR.USER_ID.eq(u.getId()))
                .fetchInto(Role.class);

        u.setRoles(roles);
    }

    void delete(Long id) {
        dsl.deleteFrom(U)
                .where(U.ID.eq(id))
                .execute();
    }

    List<User> findAll() {
        return dsl.selectFrom(U)
                .fetchInto(User.class);
    }

    void update(User user) {
        dsl.update(U)
                .set(U.USERNAME, user.getUsername())
                .set(U.PASSWORD, user.getPassword())
                .set(U.EMAIL, user.getEmail())
                .set(U.LAST_MODIFIED, now())
                .set(U.TOKEN, user.getToken())
                .set(U.STATE, user.getState().toString())
                .where(U.ID.eq(user.getId()))
                .execute();
    }

    void save(User user) {
        UsersRecord record = dsl.insertInto(U)
                .set(U.USERNAME, user.getUsername())
                .set(U.PASSWORD, user.getPassword())
                .set(U.EMAIL, user.getEmail())
                .set(U.REGISTER_DATE, user.getRegisterDate())
                .set(U.LAST_MODIFIED, user.getLastModified())
                .set(U.TOKEN, user.getToken())
                .set(U.STATE, user.getState().toString())
                .returning(U.ID)
                .fetchOne();

        user.setId(record.getValue(U.ID));
    }

    void addToken(Long id, String token) {
        dsl.update(U)
                .set(U.TOKEN, token)
                .set(U.LAST_MODIFIED, now())
                .where(U.ID.eq(id))
                .execute();
    }
}
