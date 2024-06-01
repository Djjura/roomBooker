package elfak.diplomski.RoomBooker.query.impl;

import database.Tables;
import database.tables.pojos.User;
import database.tables.records.UserRecord;
import elfak.diplomski.RoomBooker.query.IUserQuery;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(propagation = Propagation.MANDATORY)
public class UserQuery implements IUserQuery {

    @Autowired
    private DSLContext dsl;
    private static final database.tables.User USER = Tables.USER;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public User getUserByUuid(String uuid) {
        return dsl.selectFrom(USER).where(USER.UUID.eq(uuid)).fetchOneInto(User.class);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<User> getUsers() {
        List<User> users = dsl.selectFrom(USER).fetchInto(User.class);
        return users;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void insertUser(User user) {
        UUID uuid = UUID.randomUUID();
        user.setUuid(uuid.toString());
        UserRecord userRecord = dsl.newRecord(USER, user);
        dsl.insertInto(USER).set(userRecord).execute();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateUser(User user) {
        UserRecord userRecord = dsl.newRecord(USER, user);
        dsl.update(USER).set(userRecord).execute();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public User loginUser(String username, String password) {
        return dsl.selectFrom(USER)
                .where(USER.NAME.eq(username))
                .and(USER.PASSWORD.eq(password))
                .fetchOneInto(User.class);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public User getUserByUsername(String username) {
        return dsl.selectFrom(USER).where(USER.NAME.eq(username)).fetchOneInto(User.class);
    }
}
