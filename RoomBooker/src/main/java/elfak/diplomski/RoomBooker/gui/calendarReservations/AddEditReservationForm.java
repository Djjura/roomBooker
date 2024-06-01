package elfak.diplomski.RoomBooker.gui.calendarReservations;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import database.tables.pojos.Room;
import database.tables.pojos.User;
import elfak.diplomski.RoomBooker.enums.ReservationStatusEnum;
import elfak.diplomski.RoomBooker.enums.ReservationTypeEnum;
import elfak.diplomski.RoomBooker.enums.UserRoleEnum;
import elfak.diplomski.RoomBooker.models.ReservationsWithAdditionalInfo;
import elfak.diplomski.RoomBooker.query.impl.RoomQuery;
import org.vaadin.stefan.fullcalendar.TimeslotsSelectedEvent;

import java.util.List;

public class AddEditReservationForm extends FormLayout {
    RoomQuery roomQuery;

    Dialog parent;
    Room room;
    TimeslotsSelectedEvent event;
    List<Room> rooms;
    User user;

    ReservationsWithAdditionalInfo editingReservation;

    DateTimePicker startDateTime = new DateTimePicker("Start date and time");
    DateTimePicker endDateTime = new DateTimePicker("end date and time");

    TextField reservationName = new TextField("Name of reservation");

    ComboBox<ReservationTypeEnum> type = new ComboBox<>("Type");

    //    ComboBox<ReservationStatusEnum> status = new ComboBox<>("Status");
    ComboBox<Room> roomComboBox = new ComboBox<>("Rooms");
    TextField specialRequest = new TextField("Special request");

    private static boolean edit;


    public AddEditReservationForm(User user, Dialog parent, RoomQuery roomQuery, Room room, TimeslotsSelectedEvent event) {
        this.parent = parent;
        this.room = room;
        this.event = event;
        this.roomQuery = roomQuery;
        this.user = user;
        edit = false;
        add(configureForm());
    }

    public AddEditReservationForm(User user, Dialog parent, ReservationsWithAdditionalInfo reservatioon, RoomQuery roomQuery) {
        this.parent = parent;
        this.user = user;
        this.editingReservation = reservatioon;
        this.roomQuery = roomQuery;
        edit = true;
        add(configureForm());
    }

    public Component configureForm() {

        if (edit) {
            edit();
        } else {
            init();
        }
        return new VerticalLayout(roomComboBox, reservationName, startDateTime, endDateTime, type, specialRequest);
    }

    public void init() {
        setRequiredfields(true);
        if (room != null) {
            roomComboBox.setValue(room);
            roomComboBox.setReadOnly(true);

        } else {
            rooms = roomQuery.getRooms();
            roomComboBox.setItems(rooms);
            roomComboBox.setItemLabelGenerator(item -> item.getName());
        }
        type.setItems(ReservationTypeEnum.values());
        startDateTime.setValue(event.getStart());

    }

    public void edit() {
        UserRoleEnum userRoleEnum = UserRoleEnum.fromValue(user.getRole());
        if (userRoleEnum.equals(UserRoleEnum.ADMIN)) {
            setReadOnly(false);
        } else {
            setReadOnly(true);
        }
        rooms = roomQuery.getRooms();
        roomComboBox.setItems(rooms);
        roomComboBox.setItemLabelGenerator(item -> item.getName());
        for (Room room1 : rooms) {
            if (room1.getUuid().equals(editingReservation.getRoomUuid())) {
                roomComboBox.setValue(room1);
                break;
            }
        }
        reservationName.setValue(editingReservation.getName());
        startDateTime.setValue(editingReservation.getStartTime());
        endDateTime.setValue(editingReservation.getEndTime());
        type.setItems(ReservationTypeEnum.values());
        type.setValue(ReservationTypeEnum.fromValue(Integer.valueOf(editingReservation.getType())));
        specialRequest.setValue(editingReservation.getSpecialRequest());
    }

    private void setReadOnly(boolean isReadOnly) {
        roomComboBox.setReadOnly(isReadOnly);
        reservationName.setReadOnly(isReadOnly);
        startDateTime.setReadOnly(isReadOnly);
        endDateTime.setReadOnly(isReadOnly);
        type.setReadOnly(isReadOnly);
        specialRequest.setReadOnly(isReadOnly);
    }

    public void setRequiredfields(boolean isRequired) {
        roomComboBox.setRequired(isRequired);
        reservationName.setRequired(isRequired);
        startDateTime.setRequiredIndicatorVisible(isRequired);
        endDateTime.setRequiredIndicatorVisible(isRequired);
        type.setRequired(isRequired);
    }

    public ReservationsWithAdditionalInfo getReservation() {
        ReservationsWithAdditionalInfo reservation = new ReservationsWithAdditionalInfo();
        reservation.setName(reservationName.getValue());
        reservation.setType(String.valueOf(type.getValue().getType()));
        reservation.setStatus(String.valueOf(ReservationStatusEnum.RESERVED.getStatus()));
        reservation.setStartTime(startDateTime.getValue());
        reservation.setEndTime(endDateTime.getValue());
        reservation.setSpecialRequest(specialRequest.getValue());
        reservation.setRoomName(roomComboBox.getValue().getName());
        reservation.setRoomUuid(roomComboBox.getValue().getUuid());
        reservation.setUserName(user.getName());
        return reservation;

    }

}
