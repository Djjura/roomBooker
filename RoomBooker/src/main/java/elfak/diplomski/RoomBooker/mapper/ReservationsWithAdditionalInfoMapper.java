package elfak.diplomski.RoomBooker.mapper;

import database.Tables;
import elfak.diplomski.RoomBooker.models.ReservationsWithAdditionalInfo;
import org.jooq.Record;
import org.springframework.stereotype.Component;

@Component
public class ReservationsWithAdditionalInfoMapper {

    private static final database.tables.Reservations RESERVATIONS = Tables.RESERVATIONS;
    private static final database.tables.Room ROOM = Tables.ROOM;
    private static final database.tables.User USER = Tables.USER;

    public ReservationsWithAdditionalInfo fromRecord(Record record) {
        ReservationsWithAdditionalInfo rwai = new ReservationsWithAdditionalInfo();
        rwai.setUuid(record.get(RESERVATIONS.UUID));
        rwai.setName(record.get(RESERVATIONS.NAME));
        rwai.setStatus(record.get(RESERVATIONS.STATUS));
        rwai.setStartTime(record.get(RESERVATIONS.START_TIME));
        rwai.setEndTime(record.get(RESERVATIONS.END_TIME));
        rwai.setType(record.get(RESERVATIONS.TYPE));
        rwai.setRoomName(record.get(ROOM.NAME));
        rwai.setRoomUuid(record.get(RESERVATIONS.ROOM_UUID));
        rwai.setUserUuid(record.get(RESERVATIONS.USER_UUID));
        rwai.setUserName(record.get(USER.NAME));

        return rwai;
    }
}
