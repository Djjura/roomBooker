package elfak.diplomski.RoomBooker.query;

import database.tables.pojos.User;

import java.util.List;

public interface IUserQuery {
    User getUserByUuid(String uuid);

    List<User> getUsers();

    void insertUser(User user);

    void updateUser(User user);

    User loginUser(String username, String password);
}
