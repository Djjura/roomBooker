package elfak.diplomski.RoomBooker.enums;

public enum UserRoleEnum {
    USER("USER"),
    ADMIN("ADMIN");

    private String value;

    UserRoleEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static UserRoleEnum fromValue(String v) {
        if (v != null) {
            for (UserRoleEnum role : UserRoleEnum.values()) {
                if (role.getValue().equalsIgnoreCase(v)) {
                    return role;
                }
            }
        }
        throw new IllegalArgumentException("No user role found for:" + v);
    }
}
