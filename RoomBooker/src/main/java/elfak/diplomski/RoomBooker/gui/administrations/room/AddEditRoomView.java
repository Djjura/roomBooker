package elfak.diplomski.RoomBooker.gui.administrations.room;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import database.tables.pojos.Room;
import elfak.diplomski.RoomBooker.enums.RoomTypeEnum;
import elfak.diplomski.RoomBooker.query.impl.RoomQuery;
import org.springframework.beans.factory.annotation.Autowired;


public class AddEditRoomView extends FormLayout {

    TextField roomName = new TextField();
    ComboBox type = new ComboBox<RoomTypeEnum>();

    NumberField maximumOccupancy = new NumberField();

    TextField equipment = new TextField();

    Button save = new Button("Save");
    Button close = new Button("Cancel");
    Dialog parent;

    @Autowired
    private RoomQuery roomQuery;

    public AddEditRoomView(RoomQuery roomQuery, Dialog parent) {
        this.parent = parent;
        this.roomQuery = roomQuery;
        roomName.setPlaceholder("Room name...");
        type.setPlaceholder("Select type...");
        maximumOccupancy.setPlaceholder("Maximum occupancy...");
        equipment.setPlaceholder("Additional equipment...");

        add(fieldsLayout());
        init();
    }

    private void init() {
        type.setItems(RoomTypeEnum.values());
    }

    private Component fieldsLayout() {
        return new VerticalLayout(roomName, type, maximumOccupancy, equipment, buttonsLayout());
    }

    private HorizontalLayout buttonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(buttonClickEvent -> insertNewRoom());
        close.addClickListener(buttonClickEvent -> clearFieldsAndClose());

        return new HorizontalLayout(save, close);
    }

    private void clearFieldsAndClose() {
        roomName.clear();
        type.clear();
        maximumOccupancy.clear();
        equipment.clear();
        parent.close();
    }

    private void insertNewRoom() {
        Room room = new Room();
        room.setName(roomName.getValue());
        room.setType(type.getValue().toString());
        room.setMaximumOccupancy(maximumOccupancy.getValue().toString());
        room.setEquipment(equipment.getValue());
        try {
            roomQuery.insertRoom(room);
            this.clearFieldsAndClose();
            Notification notification = Notification
                    .show("Room inserted successfuly!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (Exception ex) {
            Notification notification = Notification
                    .show("An error occured while inserting the room");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
        this.clearFieldsAndClose();
    }

}
