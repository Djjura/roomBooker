package elfak.diplomski.RoomBooker.enums;

public enum RoomTypeEnum {
    AMPHITHEATRE("AMPHITHEATRE"),
    CLASSROOM("CLASSROOM"),
    CONFERENCE("CONFERENCE"),
    OTHER("OTHER");
    private String value;

    RoomTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private static RoomTypeEnum fromValue(String value) {
        if (value != null) {
            for (RoomTypeEnum roomType : RoomTypeEnum.values()) {
                if (roomType.value.equalsIgnoreCase(value)) {
                    return roomType;
                }
            }
        }
        return null;
    }
}
