package elfak.diplomski.RoomBooker.query.impl;

import database.Tables;
import database.tables.pojos.Reservations;
import database.tables.records.ReservationsRecord;
import elfak.diplomski.RoomBooker.mapper.ReservationsWithAdditionalInfoMapper;
import elfak.diplomski.RoomBooker.models.ReservationsWithAdditionalInfo;
import elfak.diplomski.RoomBooker.query.IReservationQuery;
import org.jooq.DSLContext;
import org.jooq.SelectField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(propagation = Propagation.MANDATORY)
public class ReservationQuery implements IReservationQuery {

    @Autowired
    private DSLContext dsl;
    @Autowired
    private ReservationsWithAdditionalInfoMapper mapper;

    private static final database.tables.Reservations RESERVATIONS = Tables.RESERVATIONS;
    private static final database.tables.Room ROOM = Tables.ROOM;
    private static final database.tables.User USER = Tables.USER;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Reservations getReservationByUuid(String uuid) {
        return dsl.selectFrom(RESERVATIONS).fetchOneInto(Reservations.class);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Reservations> getReservations() {
        List<Reservations> reservations = dsl.selectFrom(RESERVATIONS).fetchInto(Reservations.class);
        return reservations;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void insertReservation(ReservationsWithAdditionalInfo reservation) {
        UUID uuid = UUID.randomUUID();
        reservation.setUuid(uuid.toString());
        String roomUuid = dsl.select(ROOM.UUID)
                .from(ROOM)
                .where(ROOM.NAME.eq(reservation.getRoomName()))
                .fetchOneInto(String.class);
        if (roomUuid.isEmpty() || roomUuid.isBlank()) {
            throw new NullPointerException("No uuid for room name: " + reservation.getRoomName());
        }
        String userUuid = dsl.select(USER.UUID)
                .from(USER)
                .where(USER.NAME.eq(reservation.getUserName()))
                .fetchOneInto(String.class);

        if (userUuid.isEmpty() || userUuid.isBlank()) {
            throw new NullPointerException("No uuid for room name: " + reservation.getRoomName());
        }

        reservation.setUserUuid(userUuid);
        reservation.setRoomUuid(roomUuid);
        ReservationsRecord reservationsRecord = dsl.newRecord(RESERVATIONS, reservation);
        dsl.insertInto(RESERVATIONS).set(reservationsRecord).execute();

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateReservation(ReservationsWithAdditionalInfo reservation) {
        String roomUuid = dsl.select(ROOM.UUID)
                .from(ROOM)
                .where(ROOM.NAME.eq(reservation.getRoomName()))
                .fetchOneInto(String.class);
        if (roomUuid.isEmpty() || roomUuid.isBlank()) {
            throw new NullPointerException("No uuid for room name: " + reservation.getRoomName());
        }
        String userUuid = dsl.select(USER.UUID)
                .from(USER)
                .where(USER.NAME.eq(reservation.getUserName()))
                .fetchOneInto(String.class);

        if (userUuid.isEmpty() || userUuid.isBlank()) {
            throw new NullPointerException("No uuid for room name: " + reservation.getRoomName());
        }

        reservation.setUserUuid(userUuid);
        reservation.setRoomUuid(roomUuid);
        ReservationsRecord reservationsRecord = dsl.newRecord(RESERVATIONS, reservation);
        dsl.update(RESERVATIONS).set(reservationsRecord).execute();
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<ReservationsWithAdditionalInfo> getReservationsWithExternalData() {
        SelectField<?>[] fields = RESERVATIONS.fields();
        return dsl.select(fields).select(ROOM.NAME.as("roomName"), USER.NAME.as("userName"))
                .from(RESERVATIONS)
                .join(ROOM).on(RESERVATIONS.ROOM_UUID.eq(ROOM.UUID))
                .join(USER).on(RESERVATIONS.USER_UUID.eq(USER.UUID))
                .fetchInto(ReservationsWithAdditionalInfo.class);

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<Reservations> getRoomReservations(String uuid) {
        return dsl.selectFrom(RESERVATIONS).where(RESERVATIONS.ROOM_UUID.eq(uuid)).fetchInto(Reservations.class);
    }
}
