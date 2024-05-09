package elfak.diplomski.RoomBooker.gui.administrations.room;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import database.tables.pojos.Room;
import elfak.diplomski.RoomBooker.gui.MainView;
import elfak.diplomski.RoomBooker.query.impl.RoomQuery;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;



@PermitAll
@Route(value="room",layout = MainView.class)
@PageTitle("Rooms")
public class RoomView extends VerticalLayout {

    Grid<Room> grid = new Grid<>(Room.class);
    TextField filterText = new TextField();
    AddEditRoomView addEditRoomView;
    Dialog addEdditRoomDialog;
    Button addRoom;
    @Autowired
    RoomQuery roomQuery;

    public RoomView() {
        setSizeFull();
    }

    @PostConstruct
    private void init() {
        configureGird();
        add(toolBarLayout(), grid, createAddEdditRoomDialog());

    }

    private void configureGird() {
        grid.setSizeFull();
        grid.setColumns("name", "type", "maximumOccupancy", "equipment");
        refreshRooms();
    }

    private HorizontalLayout toolBarLayout() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);

        addRoom = new Button("Add room");
        addRoom.addClickListener(this::openDialog);

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addRoom);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private Dialog createAddEdditRoomDialog() {
        this.addEdditRoomDialog = new Dialog();
        this.addEditRoomView = new AddEditRoomView(roomQuery,addEdditRoomDialog);
        addEdditRoomDialog.setHeaderTitle("Add Room");
        FormLayout formLayout = new FormLayout(addEditRoomView);
        addEdditRoomDialog.add(formLayout);
        return addEdditRoomDialog;
    }

    private void refreshRooms(){
        List<Room> rooms = roomQuery.getRooms();
        grid.setItems(rooms);
    }

    private void openDialog(ClickEvent<Button> event) {
        addEdditRoomDialog.open();

    }
}
