package elfak.diplomski.RoomBooker.gui.administrations.reservations;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.shared.Registration;
import elfak.diplomski.RoomBooker.enums.ReservationStatusEnum;
import elfak.diplomski.RoomBooker.enums.ReservationTypeEnum;
import elfak.diplomski.RoomBooker.models.ReservationsWithAdditionalInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;

public class AddEditReservation extends FormLayout {

    private boolean isNewReservation;
    TextField name = new TextField("Name");

    //Prebaci u neki popup za biranje!!!!
    TextField userName = new TextField("User name");
    TextField roomName = new TextField("Room Name");
    DatePicker startDay = new DatePicker("Start day");
    TimePicker startTime = new TimePicker("Start time");
    DateTimePicker endTime = new DateTimePicker("End time");
    ComboBox<ReservationTypeEnum> type = new ComboBox<>("Type");

    ComboBox<ReservationStatusEnum> status = new ComboBox<>("Status");
    TextField specialRequest = new TextField("Special request");

    Button save = new Button("Save");
    Button close = new Button("Cancel");


    public AddEditReservation() {

        type.setItems(ReservationTypeEnum.values());
        status.setItems(ReservationStatusEnum.values());
        add(createFieldsLayout(), createButtonsLayout());
    }


    private Component createFieldsLayout() {

        setFieldRequirement(true);
        setDateFormat();
        return new VerticalLayout(name, userName, roomName, type, startDay, startTime, endTime, status, specialRequest);
    }

    private void setFieldRequirement(boolean isFieldRequired) {
        name.setRequired(isFieldRequired);
        endTime.setRequiredIndicatorVisible(isFieldRequired);
        type.setRequired(isFieldRequired);
        status.setRequired(isFieldRequired);
        userName.setRequired(isFieldRequired);
        roomName.setRequired(isFieldRequired);
        startDay.setRequired(isFieldRequired);
        startTime.setRequired(isFieldRequired);
    }

    private void setDateFormat() {
        Locale finnishLocale = new Locale("fi", "FI");
        startDay.setLocale(finnishLocale);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> saveReservation());
        close.addClickListener(event -> {
            fireEvent(new CloseEvent(this));
        });

        return new HorizontalLayout(save, close);
    }

    public void setReservation(ReservationsWithAdditionalInfo reservation) {
        if (reservation == null) {
            clearFormFields();
            isNewReservation = true;
            return;
        }
        isNewReservation = false;
        name.setValue(reservation.getName());
        userName.setValue(reservation.getUserName());
        roomName.setValue(reservation.getRoomName());
        type.setValue(ReservationTypeEnum.fromValue(Integer.valueOf(reservation.getType())));
        this.startDay.setValue(LocalDate.from(reservation.getStartTime()));
        this.startTime.setValue(LocalTime.from(reservation.getStartTime()));
        status.setValue(ReservationStatusEnum.fromValue(Integer.valueOf(reservation.getStatus())));
        specialRequest.setValue(reservation.getSpecialRequest());
    }

    private void clearFormFields() {
        name.clear();
        userName.clear();
        roomName.clear();
        type.clear();
        startDay.clear();
        startTime.clear();
        endTime.clear();
        status.clear();
        specialRequest.clear();
    }


    private void saveReservation() {
        ReservationsWithAdditionalInfo reservations = new ReservationsWithAdditionalInfo();
        reservations.setName(name.getValue());
        reservations.setType(String.valueOf(type.getValue().getType()));
        reservations.setStatus(String.valueOf(status.getValue().getStatus()));
        reservations.setStartTime(LocalDateTime.of(startDay.getValue(), startTime.getValue()));
        reservations.setEndTime(endTime.getValue());
        reservations.setSpecialRequest(specialRequest.getValue());
        reservations.setRoomName(roomName.getValue());
        reservations.setUserName(userName.getValue());
        if (isNewReservation) {
            fireEvent(new SaveEvent(this, reservations));
        } else {
            fireEvent(new EditEvent(this, reservations));
        }

    }

    public abstract static class ReservationFormEvent extends ComponentEvent<AddEditReservation> {
        private ReservationsWithAdditionalInfo reservationsWithAdditionalInfo;

        protected ReservationFormEvent(AddEditReservation source, ReservationsWithAdditionalInfo reservationsWithAdditionalInfo) {
            super(source, false);
            this.reservationsWithAdditionalInfo = reservationsWithAdditionalInfo;
        }

        public ReservationsWithAdditionalInfo getReservationsWithAdditionalInfo() {
            return reservationsWithAdditionalInfo;
        }
    }

    public static class SaveEvent extends AddEditReservation.ReservationFormEvent {
        SaveEvent(AddEditReservation source, ReservationsWithAdditionalInfo reservationsWithAdditionalInfo) {
            super(source, reservationsWithAdditionalInfo);
        }
    }

    public static class CloseEvent extends AddEditReservation.ReservationFormEvent {
        CloseEvent(AddEditReservation source) {
            super(source, null);
        }
    }

    public static class EditEvent extends AddEditReservation.ReservationFormEvent {
        public EditEvent(AddEditReservation source, ReservationsWithAdditionalInfo reservationsWithAdditionalInfo) {
            super(source, reservationsWithAdditionalInfo);
        }
    }

    public Registration addSaveListener(ComponentEventListener<AddEditReservation.SaveEvent> listener) {
        return addListener(AddEditReservation.SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<AddEditReservation.CloseEvent> listener) {
        return addListener(AddEditReservation.CloseEvent.class, listener);
    }

    public Registration addEditListener(ComponentEventListener<AddEditReservation.EditEvent> listener) {
        return addListener(AddEditReservation.EditEvent.class, listener);
    }
}
