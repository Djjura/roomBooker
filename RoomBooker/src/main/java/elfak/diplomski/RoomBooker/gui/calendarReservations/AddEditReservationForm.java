package elfak.diplomski.RoomBooker.gui.calendarReservations;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import database.tables.pojos.Room;
import elfak.diplomski.RoomBooker.enums.ReservationStatusEnum;
import elfak.diplomski.RoomBooker.enums.ReservationTypeEnum;
import elfak.diplomski.RoomBooker.query.impl.ReservationQuery;
import elfak.diplomski.RoomBooker.query.impl.RoomQuery;
import org.vaadin.stefan.fullcalendar.TimeslotsSelectedEvent;

import java.util.List;

public class AddEditReservationForm extends FormLayout {

    ReservationQuery reservationQuery;
    RoomQuery roomQuery;

    Dialog parent;
    Room room;
    TimeslotsSelectedEvent event;
    List<Room> rooms;

    DateTimePicker startDateTime = new DateTimePicker("start date and time");

    TextField reservationName = new TextField("Name of reservation");

    NumberField duration = new NumberField("Duration in minutes");
    ComboBox<ReservationTypeEnum> type = new ComboBox<>("Type");

    ComboBox<ReservationStatusEnum> status = new ComboBox<>("Status");
    ComboBox<Room> roomComboBox = new ComboBox<>("Rooms");
    TextField specialRequest = new TextField("Special request");

    TextField roomName = new TextField("Room name");


    public AddEditReservationForm(ReservationQuery reservationQuery, Dialog parent, RoomQuery roomQuery, Room room, TimeslotsSelectedEvent event) {
        this.reservationQuery = reservationQuery;
        this.parent = parent;
        this.room = room;
        this.event = event;
        this.roomQuery = roomQuery;
        add(configureForm());
    }

    public Component configureForm() {

        init();
        return new VerticalLayout(roomName, roomComboBox, reservationName, startDateTime, duration, type, status, specialRequest);
    }

    public void init() {
        if (room != null) {
            roomName.setReadOnly(true);
            roomName.setVisible(true);
            roomName.setValue(room.getName());

            roomComboBox.setVisible(false);
        } else {
            roomName.setVisible(false);
            roomComboBox.setVisible(true);
            rooms = roomQuery.getRooms();
            roomComboBox.setItems(rooms);
            roomComboBox.setItemLabelGenerator(item -> item.getName());
        }
        startDateTime.setValue(event.getStart());

    }

}
