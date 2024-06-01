package elfak.diplomski.RoomBooker.gui.administrations.reservations;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import elfak.diplomski.RoomBooker.gui.MainView;
import elfak.diplomski.RoomBooker.models.ReservationsWithAdditionalInfo;
import elfak.diplomski.RoomBooker.query.impl.ReservationQuery;
import jakarta.annotation.security.PermitAll;

import java.util.List;

@PermitAll
@Route(value = "reservations", layout = MainView.class)
@PageTitle("Reservations")
public class ReservationsView extends VerticalLayout {
    Grid<ReservationsWithAdditionalInfo> grid = new Grid<>(ReservationsWithAdditionalInfo.class);
    TextField filterText = new TextField();

    AddEditReservation addEditReservation;

    ReservationQuery reservationQuery;

    public ReservationsView(ReservationQuery reservationQuery) {
        this.reservationQuery = reservationQuery;
        setSizeFull();
        configureForm();
        configureGird();

        add(toolBarLayout(), getContetnt());

    }

    private Component getContetnt() {
        HorizontalLayout layout = new HorizontalLayout(grid, addEditReservation);
        layout.setFlexGrow(2, grid);
        layout.setFlexGrow(1, addEditReservation);
        layout.setSizeFull();
        return layout;
    }

    private void configureForm() {
        addEditReservation = new AddEditReservation();
        addEditReservation.setWidth("25em");
        addEditReservation.addSaveListener(this::saveNewReservation);
        addEditReservation.addCloseListener(e -> closeEditor());
        addEditReservation.addEditListener(this::saveEditedReservation);
    }

    private void closeEditor() {
        addEditReservation.setReservation(null);
        addEditReservation.setVisible(false);
        removeClassName("editing");
    }

    private void saveEditedReservation(AddEditReservation.EditEvent editEvent) {
        reservationQuery.updateReservation(editEvent.getReservationsWithAdditionalInfo());
        refreshReservations();
    }

    private void saveNewReservation(AddEditReservation.SaveEvent saveEvent) {
        reservationQuery.insertReservation(saveEvent.getReservationsWithAdditionalInfo());
        refreshReservations();
    }

    private Component toolBarLayout() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);

        Button addReservation = new Button("Add reservation");
        addReservation.addClickListener(click -> addReservation());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addReservation);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addReservation() {
        grid.asSingleSelect().clear();
        addEditReservation.setVisible(true);
        addEditReservation.setReservation(null);
        addClassName("editing");
    }

    private void configureGird() {
        grid.setSizeFull();
        grid.setColumns("name", "userName", "type", "status", "startTime", "endTime", "specialRequest", "roomName");
        refreshReservations();

        grid.asSingleSelect().addValueChangeListener(event ->
                editReservation(event.getValue()));
    }

    private void editReservation(ReservationsWithAdditionalInfo reservationsWithAdditionalInfo) {
        if (reservationsWithAdditionalInfo == null) {
            closeEditor();
        } else {
            addEditReservation.setReservation(reservationsWithAdditionalInfo);
            addEditReservation.setVisible(true);
            addClassName("editing");
        }
    }

    private void refreshReservations() {
        List<ReservationsWithAdditionalInfo> reservations = reservationQuery.getReservationsWithExternalData();
        grid.setItems(reservations);
    }
}
