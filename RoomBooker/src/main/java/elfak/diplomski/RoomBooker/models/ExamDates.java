package elfak.diplomski.RoomBooker.models;

import database.tables.pojos.Exam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExamDates {

    private LocalDate examDay;

    private List<Exam> examLIst;

    public ExamDates() {
    }

    public ExamDates(LocalDate examDay) {
        this.examDay = examDay;
        this.examLIst = new ArrayList<>();
    }

    public ExamDates(LocalDate examDay, List<Exam> examLIst) {
        this.examDay = examDay;
        this.examLIst = examLIst;
    }

    public LocalDate getExamDay() {
        return examDay;
    }

    public void setExamDay(LocalDate examDay) {
        this.examDay = examDay;
    }

    public List<Exam> getExamLIst() {
        return examLIst;
    }

    public void setExamLIst(List<Exam> examLIst) {
        this.examLIst = examLIst;
    }
}
