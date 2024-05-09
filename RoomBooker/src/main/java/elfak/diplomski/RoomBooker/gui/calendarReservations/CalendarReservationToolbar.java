package elfak.diplomski.RoomBooker.gui.calendarReservations;

import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import database.tables.pojos.Room;
import elfak.diplomski.RoomBooker.query.impl.RoomQuery;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.stefan.fullcalendar.CalendarView;
import org.vaadin.stefan.fullcalendar.CalendarViewImpl;
import org.vaadin.stefan.fullcalendar.FullCalendar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class CalendarReservationToolbar extends MenuBar {

    FullCalendar calendar;
    MenuItem viewSelector;
    MenuItem roomSelector;

    RoomQuery roomQuery;

    CalendarReservationsView parent;

    public CalendarReservationToolbar(FullCalendar calendar, RoomQuery roomQuery, CalendarReservationsView parent) {
        this.calendar = calendar;
        this.roomQuery = roomQuery;
        this.parent = parent;

        DatePicker goToDate = new DatePicker();
        goToDate.addValueChangeListener(event -> {
            calendar.gotoDate(event.getValue());
        });
        addItem(VaadinIcon.ANGLE_LEFT.create(), e -> calendar.previous()).setId("period-previous-button");
        addItem(goToDate);
        addItem(VaadinIcon.ANGLE_RIGHT.create(), e -> calendar.next());
        addItem("Today", e -> calendar.today());
        initViewSelector();
        initRoomSelector();
    }

    private void initRoomSelector() {
        List<Room> rooms = roomQuery.getRooms();

        String roomName = parent.getCurrentlyActiveRoom() != null ?
                parent.getCurrentlyActiveRoom().getName() : "All rooms";

        rooms.sort(Comparator.comparing(Room::getName));
        roomSelector = addItem("Reservations for room:" + roomName);
        SubMenu subMenu = roomSelector.getSubMenu();
        rooms.stream().sorted(Comparator.comparing(Room::getName)).forEach(room -> {
            subMenu.addItem("Reservations for room: " + room.getName(), event -> refreshCalendarEntryies(room));
        });
    }

    private void refreshCalendarEntryies(Room room) {
        roomSelector.setText("Reservations for room: " + room.getName());
        parent.setCurrentlyActiveRoom(room);
        calendar.getEntryProvider().asInMemory().removeAllEntries();
        calendar.getEntryProvider().asInMemory().addEntries(parent.getInitialEntries(room));
        calendar.getEntryProvider().refreshAll();
    }


    private void initViewSelector() {
        List<CalendarView> calendarViews;

        calendarViews = new ArrayList<>(Arrays.asList(CalendarViewImpl.values()));


        calendarViews.sort(Comparator.comparing(CalendarView::getName));

        viewSelector = addItem("View: " + getViewName(calendar.getCurrentView().orElse(CalendarViewImpl.DAY_GRID_MONTH)));
        SubMenu subMenu = viewSelector.getSubMenu();
        calendarViews.stream()
                .sorted(Comparator.comparing(this::getViewName))
                .forEach(view -> {
                    String viewName = getViewName(view);
                    subMenu.addItem(viewName, event -> {
                        calendar.changeView(view);
                        viewSelector.setText("View: " + viewName);
                    });
                });
    }

    private String getViewName(CalendarView view) {
        String name = null /*customViewNames.get(view)*/;
        if (name == null) {
            name = StringUtils.capitalize(String.join(" ", StringUtils.splitByCharacterTypeCamelCase(view.getClientSideValue())));
        }

        return name;
    }

    public void updateSelectedView(CalendarView view) {
        if (viewSelector != null) {
            viewSelector.setText("View: " + getViewName(view));
        }
    }

}
