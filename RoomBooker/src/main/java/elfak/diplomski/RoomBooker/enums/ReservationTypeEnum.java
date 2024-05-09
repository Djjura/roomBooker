package elfak.diplomski.RoomBooker.enums;

public enum ReservationTypeEnum {
    EXAM(0),
    LECTURE(1),
    CONFERENCE(2);

    private int type;

    public int getType() {
        return type;
    }

    ReservationTypeEnum(int i) {
        type = i;
    }

    public static ReservationTypeEnum fromValue(Integer value) {
        if (value != null) {
            for (ReservationTypeEnum reservation : ReservationTypeEnum.values()) {
                if (reservation.getType() == value) {
                    return reservation;
                }
            }
        }
        throw new IllegalArgumentException("No valid enum type for value: " + value);
    }
}
