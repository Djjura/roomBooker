package elfak.diplomski.RoomBooker.gui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import elfak.diplomski.RoomBooker.gui.administrations.reservations.ReservationsView;
import elfak.diplomski.RoomBooker.gui.administrations.room.RoomView;
import elfak.diplomski.RoomBooker.gui.administrations.user.UserView;
import elfak.diplomski.RoomBooker.gui.calendarReservations.CalendarReservationsView;
import elfak.diplomski.RoomBooker.security.SecurityService;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route("")
public class MainView extends AppLayout {

    private final SecurityService securityService;

    public MainView(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Room Booker");
        logo.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);

        String u = securityService.getAuthenticatedUser().getUsername();
        Button logout = new Button("Log out " + u, e -> securityService.logout());

        var header = new HorizontalLayout(new DrawerToggle(), logo, logout);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);
        addToNavbar(header);
    }

    private void createDrawer() {
        addToDrawer(new VerticalLayout(
                new RouterLink("Rooms", RoomView.class)
                , new RouterLink("Users", UserView.class)
                , new RouterLink("Reservations", ReservationsView.class)
                , new RouterLink("Create reservation", CalendarReservationsView.class)
        ));
    }
}
