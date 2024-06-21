package elfak.diplomski.RoomBooker.models;

import database.tables.pojos.ExamTerm;

public class ExamTermEventWrapper {

    private ExamTerm examTerm;
    private String examName;

    public ExamTerm getExamTerm() {
        return examTerm;
    }

    public void setExamTerm(ExamTerm examTerm) {
        this.examTerm = examTerm;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }
}
