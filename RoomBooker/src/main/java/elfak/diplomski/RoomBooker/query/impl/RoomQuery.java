package elfak.diplomski.RoomBooker.query.impl;

import database.Tables;
import database.tables.pojos.Room;
import database.tables.records.RoomRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import elfak.diplomski.RoomBooker.query.IRoomQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(propagation = Propagation.MANDATORY)
public class RoomQuery implements IRoomQuery {


    @Autowired
    private DSLContext dsl;

    private static final database.tables.Room ROOM = Tables.ROOM;

    public RoomQuery() {
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Room getRoomByUuid(String uuid) {
        return dsl.selectFrom(ROOM).fetchOneInto(Room.class);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Room> getRooms() {
        List<Room> rooms = dsl.selectFrom(ROOM).fetchInto(Room.class);
        return rooms;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void insertRoom(Room room) {
        UUID uuid = UUID.randomUUID();
        room.setUuid(uuid.toString());
        RoomRecord roomRecord = dsl.newRecord(ROOM, room);
        dsl.insertInto(ROOM).set(roomRecord).execute();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateRoom(Room room) {
        RoomRecord roomRecord = dsl.newRecord(ROOM, room);
        dsl.update(ROOM).set(roomRecord).execute();
    }
}
