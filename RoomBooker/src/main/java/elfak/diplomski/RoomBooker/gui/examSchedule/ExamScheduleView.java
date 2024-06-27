package elfak.diplomski.RoomBooker.gui.examSchedule;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import database.tables.pojos.Term;
import elfak.diplomski.RoomBooker.gui.MainView;
import elfak.diplomski.RoomBooker.gui.calendarReservations.CalendarReservationToolbar;
import elfak.diplomski.RoomBooker.models.ExamTermEventWrapper;
import elfak.diplomski.RoomBooker.query.impl.TermQuery;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.stefan.fullcalendar.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@PermitAll
@Route(value = "exam-schedule", layout = MainView.class)
@PageTitle("Exam schedule")
public class ExamScheduleView extends VerticalLayout {

    private FullCalendar calendar;
    private CalendarReservationToolbar toolbar;

    private Button createTermBtn = new Button("Create term");
    private Button addExamBtn = new Button("Add exam");

    private ComboBox<Term> terms = new ComboBox<>();

    @Autowired
    private TermQuery termQuery;

    public ExamScheduleView(TermQuery termQuery) {
        this.termQuery = termQuery;
        configureCalendar();
        configureTerm();
        configureExamButton();
        HorizontalLayout layout = new HorizontalLayout(createTermBtn, terms, addExamBtn);
        setSizeFull();
        add(layout, calendar);
    }

    private void configureTerm() {
        this.createTermBtn.addClickListener(event -> {
            UI.getCurrent().navigate(CreateTermView.class);
        });
        List<Term> termsList = termQuery.getTerms();
        terms.setItems(termsList);
        terms.setItemLabelGenerator(item -> item.getName());

    }

    private void configureExamButton() {
        addExamBtn.addClickListener(event -> {
            if (terms.getValue() == null) {
                Notification notification = new Notification();
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setText("Term needs to be selected if you want to add exams");
                notification.open();
                return;
            }
//            UI.getCurrent().navigate(AddExamToTermView.class);
        });
    }

    private void configureCalendar() {

        calendar = FullCalendarBuilder.create()
                .withAutoBrowserLocale().build();
        calendar.setBusinessHours(new BusinessHours(LocalTime.of(9, 0), LocalTime.of(20, 0), BusinessHours.DEFAULT_BUSINESS_WEEK));
        calendar.setSizeFull();
        calendar.setTimeslotsSelectable(true);
        calendar.changeView(CalendarViewImpl.DAY_GRID_MONTH);
        calendar.setSlotMinTime(LocalTime.of(7, 0));
        calendar.setSlotMaxTime(LocalTime.of(17, 0));
        calendar.getEntryProvider().asInMemory().addEntries(getInitialEntries());

        terms.addValueChangeListener(event -> {
            setNewCalendarEntires(event.getValue());
        });

    }


    private void setNewCalendarEntires(Term term) {
        calendar.getEntryProvider().asInMemory().removeAllEntries();
        calendar.getEntryProvider().asInMemory().addEntries(getInitialEntries());
        calendar.getEntryProvider().refreshAll();
    }

    private Iterable<Entry> getInitialEntries() {
        Term term = terms.getValue();
        List<Entry> entries = new ArrayList<>();
        if (term == null) {
            return entries;
        }
        List<ExamTermEventWrapper> exams = termQuery.getExamsForTerm(term);


        exams.forEach(element -> {
            Entry entry = new Entry(element.getExamTerm().getUuid());
            entry.setTitle(element.getExamName());
            entry.setDisplayMode(DisplayMode.BLOCK);
            entry.setAllDay(true);
            entry.setStart(element.getExamTerm().getDate());
            entry.setEnd(element.getExamTerm().getDate());
            entries.add(entry);
        });
        return entries;
    }
}
