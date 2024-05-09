package elfak.diplomski.RoomBooker.query;

import database.tables.pojos.Room;


import java.util.List;


public interface IRoomQuery {

    Room getRoomByUuid(String uuid);

    List<Room> getRooms();

    void insertRoom(Room room);

    void updateRoom(Room room);
}
