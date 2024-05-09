package elfak.diplomski.RoomBooker.gui.calendarReservations;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import database.tables.pojos.Reservations;
import database.tables.pojos.Room;
import elfak.diplomski.RoomBooker.gui.MainView;
import elfak.diplomski.RoomBooker.query.impl.ReservationQuery;
import elfak.diplomski.RoomBooker.query.impl.RoomQuery;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.stefan.fullcalendar.*;


import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@PermitAll
@Route(value = "create-reservation", layout = MainView.class)
@PageTitle("Create Reservation")
public class CalendarReservationsView extends VerticalLayout {

    private FullCalendar calendar;
    private CalendarReservationToolbar toolbar;
    private AddEditReservationForm reservationForm;

    private Room currentlyActiveRoom;

    @Autowired
    private ReservationQuery reservationQuery;
    @Autowired
    private RoomQuery roomQuery;

    public CalendarReservationsView(ReservationQuery reservationQuery, RoomQuery roomQuery) {
        this.reservationQuery = reservationQuery;
        this.roomQuery = roomQuery;

        configureCalendar();
    }

    @PostConstruct
    private void configToolbar() {
        toolbar = new CalendarReservationToolbar(calendar, roomQuery, this);
        setSizeFull();
        add(toolbar, calendar);
    }


    private void configureCalendar() {


        calendar = FullCalendarBuilder.create()
                .withAutoBrowserLocale()
                .withAutoBrowserTimezone()
                .withInitialEntries(getInitialEntries(null)).build();
        calendar.setBusinessHours(new BusinessHours(LocalTime.of(9, 0), LocalTime.of(20, 0), BusinessHours.DEFAULT_BUSINESS_WEEK));
        calendar.setSizeFull();
        calendar.setTimeslotsSelectable(true);
        calendar.changeView(CalendarViewImpl.TIME_GRID_WEEK);
        calendar.setSlotMinTime(LocalTime.of(7, 0));
        calendar.setSlotMaxTime(LocalTime.of(17, 0));

        calendar.addTimeslotsSelectedListener((event) -> {
//            Entry entry = new Entry();

//            entry.setStart(event.getStart());
//            entry.setEnd(event.getEnd());
//            entry.setAllDay(event.isAllDay());
//
//            entry.setColor("dodgerblue");

            Dialog dialog = new Dialog("Create reservation");
            AddEditReservationForm addEditReservationForm = new AddEditReservationForm(reservationQuery, dialog, roomQuery, currentlyActiveRoom, event);
            dialog.add(addEditReservationForm);
            dialog.open();

        });

        calendar.addEntryClickedListener((event) -> {
            // react on the clicked entry, for instance let the user edit it
            Entry entry = event.getEntry();
            entry.getId();


            // ... show an editor
        });
    }

    public List<Entry> getInitialEntries(Room room) {
        List<Reservations> reservations;
        if (room == null) {
            reservations = reservationQuery.getReservations();
        } else {
            reservations = reservationQuery.getRoomReservations(room.getUuid());
        }
        List<Entry> entries = new ArrayList<>();
        reservations.forEach(reservation -> {
            Entry entry = new Entry(reservation.getUuid());
            entry.setTitle(reservation.getName());
            entry.setStartEditable(true);
            entry.setDisplayMode(DisplayMode.BLOCK);
            entry.setStart(reservation.getStartTime()); // Start time
            entry.setEnd(reservation.getStartTime().plusMinutes(reservation.getDuration()));
            entries.add(entry);
        });
        return entries;
    }

    public void setCurrentlyActiveRoom(Room room) {
        currentlyActiveRoom = room;
    }

    public Room getCurrentlyActiveRoom() {
        return currentlyActiveRoom;
    }
}
