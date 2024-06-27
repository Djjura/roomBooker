package elfak.diplomski.RoomBooker.gui.examSchedule;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import database.tables.pojos.Exam;
import database.tables.pojos.ExamTerm;
import database.tables.pojos.Term;
import elfak.diplomski.RoomBooker.gui.MainView;
import elfak.diplomski.RoomBooker.query.impl.TermQuery;
import elfak.diplomski.RoomBooker.service.ExamSchedulerService;
import jakarta.annotation.security.PermitAll;

import java.util.List;

@PermitAll
@Route(value = "create-exam-term", layout = MainView.class)
@PageTitle("Create exam term")
public class AddExamToTermView extends VerticalLayout {

    private TermQuery termQuery;

    private ExamSchedulerService examSchedulerService;

    private ComboBox<Exam> examCombobox = new ComboBox<>("Select exam");
    private ComboBox<Term> termComboBox = new ComboBox<>("Select term");

    private NumberField studentsApplied = new NumberField("Students applied");

    Button addExamBtn = new Button("Add exam");
    Button createExamSchedule = new Button("Create exam schedule");
    Button cancel = new Button("Cancel");

    public AddExamToTermView(TermQuery termQuery, ExamSchedulerService ex) {
        this.termQuery = termQuery;
        this.examSchedulerService = ex;
        config();
        HorizontalLayout layout = new HorizontalLayout(addExamBtn, cancel, createExamSchedule);
        add(examCombobox, termComboBox, studentsApplied, layout);
    }

    private void config() {
        List<Term> terms = termQuery.getTerms();
        termComboBox.setItems(terms);
        termComboBox.setItemLabelGenerator(item -> item.getName());
        List<Exam> exams = termQuery.getExams();
        examCombobox.setItems(exams);
        examCombobox.setItemLabelGenerator(item -> item.getName());

        addExamBtn.addClickListener(event -> {
            ExamTerm examTerm = new ExamTerm();
            examTerm.setExamId(examCombobox.getValue().getId());
            examTerm.setTermId(termComboBox.getValue().getUuid());
            examTerm.setStudentsApplied(studentsApplied.getValue().intValue());
            termQuery.insertExam(examTerm);
            Notification notification = Notification.show("Exam successfully added");
            notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
            notification.open();
        });

        createExamSchedule.addClickListener(event -> {
            examSchedulerService.generateTermShedule(termComboBox.getValue());
        });
    }
}
