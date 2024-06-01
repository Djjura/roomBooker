package elfak.diplomski.RoomBooker.gui.calendarReservations;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import database.tables.pojos.Reservations;
import database.tables.pojos.Room;
import database.tables.pojos.User;
import elfak.diplomski.RoomBooker.enums.UserRoleEnum;
import elfak.diplomski.RoomBooker.gui.MainView;
import elfak.diplomski.RoomBooker.models.ReservationsWithAdditionalInfo;
import elfak.diplomski.RoomBooker.query.impl.ReservationQuery;
import elfak.diplomski.RoomBooker.query.impl.RoomQuery;
import elfak.diplomski.RoomBooker.query.impl.UserQuery;
import elfak.diplomski.RoomBooker.security.SecurityService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.stefan.fullcalendar.*;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


@PermitAll
@Route(value = "create-reservation", layout = MainView.class)
@PageTitle("Create Reservation")
public class CalendarReservationsView extends VerticalLayout {
    private final SecurityService securityService;
    private FullCalendar calendar;
    private CalendarReservationToolbar toolbar;
    private AddEditReservationForm reservationForm;

    private Room currentlyActiveRoom;

    @Autowired
    private ReservationQuery reservationQuery;
    @Autowired
    private RoomQuery roomQuery;
    @Autowired
    private UserQuery userQuery;

    private final Timezone timezone = new Timezone(ZoneId.systemDefault());
    private User user;

    public CalendarReservationsView(ReservationQuery reservationQuery, RoomQuery roomQuery, SecurityService securityService, UserQuery userQuery) {
        this.reservationQuery = reservationQuery;
        this.roomQuery = roomQuery;
        this.securityService = securityService;
        this.userQuery = userQuery;
        user = userQuery.getUserByUsername(securityService.getAuthenticatedUser().getUsername());
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
                .withAutoBrowserLocale().build();
        calendar.setBusinessHours(new BusinessHours(LocalTime.of(9, 0), LocalTime.of(20, 0), BusinessHours.DEFAULT_BUSINESS_WEEK));
        calendar.setSizeFull();
        calendar.setTimeslotsSelectable(true);
        calendar.changeView(CalendarViewImpl.TIME_GRID_WEEK);
        calendar.setSlotMinTime(LocalTime.of(7, 0));
        calendar.setSlotMaxTime(LocalTime.of(17, 0));
        calendar.getEntryProvider().asInMemory().addEntries(getInitialEntries(null));


        calendar.addTimeslotsSelectedListener((event) -> {

            Dialog dialog = new Dialog("Create reservation");
            AddEditReservationForm addEditReservationForm = new AddEditReservationForm(user, dialog, roomQuery, currentlyActiveRoom, event);
            Button saveButton = new Button("Save", event1 -> saveResevation(dialog, addEditReservationForm));
            Button cancelButton = new Button("Cancel", e -> dialog.close());

            dialog.add(addEditReservationForm);
            dialog.getFooter().add(saveButton);
            dialog.getFooter().add(cancelButton);
            dialog.open();

        });

        calendar.addEntryClickedListener((event) -> {
            UserRoleEnum userRole = UserRoleEnum.fromValue(user.getRole());
            ReservationsWithAdditionalInfo reservation = reservationQuery.getReservationWithExternalData(event.getEntry().getId());
            if (UserRoleEnum.fromValue(user.getRole()).equals(UserRoleEnum.ADMIN)) {
                Dialog dialog = new Dialog("Edit reservation");
                AddEditReservationForm addEditReservationForm = new AddEditReservationForm(user, dialog, reservation, roomQuery);
                Button editButton = new Button("Edit", editEvent -> editreservation(dialog, addEditReservationForm));
                Button deleteButton = new Button("Delete", deleteEvent -> deleteReservation(dialog, addEditReservationForm));
                Button cancelButton = new Button("Cancel", e -> dialog.close());

                dialog.add(addEditReservationForm);
                dialog.getFooter().add(editButton);
                dialog.getFooter().add(cancelButton);
                dialog.open();
            } else {
                Dialog dialog = new Dialog("View reservation");
                AddEditReservationForm addEditReservationForm = new AddEditReservationForm(user, dialog, reservation, roomQuery);
                Button cancelButton = new Button("Cancel", e -> dialog.close());

                dialog.add(addEditReservationForm);
                dialog.getFooter().add(cancelButton);
                dialog.open();
            }


        });
    }

    private void deleteReservation(Dialog dialog, AddEditReservationForm addEditReservationForm) {
        reservationQuery.deleteReservation(addEditReservationForm.getReservation().getUuid());
        dialog.close();
    }

    private void editreservation(Dialog dialog, AddEditReservationForm reservationForm) {
        ReservationsWithAdditionalInfo reservation = reservationForm.getReservation();
        reservationQuery.updateReservation(reservation);
        dialog.close();
    }

    public void saveResevation(Dialog dialog, AddEditReservationForm reservationForm) {
        ReservationsWithAdditionalInfo reservation = reservationForm.getReservation();
        reservationQuery.insertReservation(reservation);
        dialog.close();
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
            entry.setStartWithOffset(reservation.getStartTime());
            entry.setEndWithOffset(reservation.getEndTime());
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
