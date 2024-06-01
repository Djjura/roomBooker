package elfak.diplomski.RoomBooker.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ExternalCalendarRequest implements Serializable {

    static final long serialVersionUID = 42L;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }
}
