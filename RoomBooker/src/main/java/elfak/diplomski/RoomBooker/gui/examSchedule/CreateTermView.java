package elfak.diplomski.RoomBooker.gui.examSchedule;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import database.tables.pojos.Term;
import elfak.diplomski.RoomBooker.gui.MainView;
import elfak.diplomski.RoomBooker.query.impl.TermQuery;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route(value = "create-term", layout = MainView.class)
@PageTitle("Create term")
public class CreateTermView extends VerticalLayout {
    DatePicker startDateTime = new DatePicker("Start date");
    DatePicker endDateTime = new DatePicker("End date");

    TextField termName = new TextField("Term name");

    Button createTerm = new Button("Create term");
    Button cancelTerm = new Button("Cancel");
    private TermQuery termQuery;


    public CreateTermView(TermQuery termQuery) {
        this.termQuery = termQuery;
        createTerm.addClickListener(e -> {
            createTerm();
        });
        HorizontalLayout layout = new HorizontalLayout(createTerm, cancelTerm);
        add(termName, startDateTime, endDateTime, layout);
    }

    private ComponentEventListener<ClickEvent<Button>> createTerm() {
        Term term = new Term();
        term.setName(termName.getValue());
        term.setStartDate(startDateTime.getValue());
        term.setEndDate(endDateTime.getValue());
        if (term.getStartDate().isAfter(term.getEndDate())) {
            Notification notification = new Notification();
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setText("Start date is after end date !");
            notification.open();
            return null;
        }
        termQuery.insertTerm(term);
        Notification notification = Notification.show("Term successfully created");
        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
        return null;
    }
}
