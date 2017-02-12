package com.grudus.examshelper.users;


import com.grudus.examshelper.tables.Roles;
import com.grudus.examshelper.tables.UserRoles;
import com.grudus.examshelper.tables.Users;
import com.grudus.examshelper.tables.records.UsersRecord;
import com.grudus.examshelper.users.roles.Role;
import com.grudus.examshelper.users.roles.RoleName;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.grudus.examshelper.users.UserState.ENABLED;
import static com.grudus.examshelper.users.UserState.WAITING;
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

    public Optional<User> findByEmail(String email) {
        return dsl.selectFrom(U)
                .where(U.EMAIL.eq(email))
                .fetchOptionalInto(User.class);
    }

    Optional<User> findEnabledByUsername(String username) {
        return dsl.selectFrom(U)
                .where(U.USERNAME.eq(username).and(U.STATE.eq(ENABLED.toString())))
                .fetchOptionalInto(User.class);
    }

    Optional<User> findByTokenWithState(String token, UserState state) {
        return dsl.selectFrom(U)
                .where(U.TOKEN.eq(token).and(U.STATE.eq(state.toString())))
                .fetchOptionalInto(User.class);
    }

    Optional<User> findById(Long id) {
        return dsl.selectFrom(U)
                .where(U.ID.eq(id))
                .fetchOptionalInto(User.class);
    }

    void fetchUserRoles(User u) {
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


    void addRoles(User user, List<RoleName> roleNames) {
        List<Role> roles = dsl.selectFrom(R).where(R.NAME.in(roleNames)).fetchInto(Role.class);

        BatchBindStep batch = dsl.batch(dsl.insertInto(UR, UR.USER_ID, UR.ROLE_ID).values((Long) null, null));
        roles.forEach(role -> batch.bind(user.getId(), role.getId()));

        batch.execute();
    }

    public void saveAddUserRequest(String username, String password, String email, String token) {
        dsl.insertInto(U)
                .set(U.USERNAME, username)
                .set(U.PASSWORD, password)
                .set(U.EMAIL, email)
                .set(U.REGISTER_DATE, now())
                .set(U.LAST_MODIFIED, now())
                .set(U.TOKEN, token)
                .set(U.STATE, WAITING.toString())
                .execute();
    }

    public void updateState(Long id, UserState state) {
        dsl.update(U)
                .set(U.STATE, state.toString())
                .set(U.LAST_MODIFIED, now())
                .where(U.ID.eq(id))
                .execute();
    }
}
