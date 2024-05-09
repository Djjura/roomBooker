package elfak.diplomski.RoomBooker.enums;

public enum ReservationStatusEnum {
    RESERVED(0),
    CANCELED(1),
    FINISHED(2);

    private int status;

    public int getStatus() {
        return status;
    }

    ReservationStatusEnum(int i) {
        status = i;
    }

    public static ReservationStatusEnum fromValue(Integer value) {
        if (value != null) {
            for (ReservationStatusEnum reservation : ReservationStatusEnum.values()) {
                if (reservation.getStatus() == value) {
                    return reservation;
                }
            }
        }
        throw new IllegalArgumentException("No valid enum type for value: " + value);
    }
}
