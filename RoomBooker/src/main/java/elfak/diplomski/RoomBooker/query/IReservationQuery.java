package elfak.diplomski.RoomBooker.query;

import database.tables.pojos.Reservations;
import elfak.diplomski.RoomBooker.models.ReservationsWithAdditionalInfo;

import java.util.List;

public interface IReservationQuery {

    Reservations getReservationByUuid(String uuid);

    List<Reservations> getReservations();

    void insertReservation(ReservationsWithAdditionalInfo reservation);

    void updateReservation(ReservationsWithAdditionalInfo reservation);

    public List<ReservationsWithAdditionalInfo> getReservationsWithExternalData();

    public List<Reservations> getRoomReservations(String uuid);
    ReservationsWithAdditionalInfo getReservationWithExternalData(String uuid);

    void deleteReservation(String uuid);
}
