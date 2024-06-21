package elfak.diplomski.RoomBooker.query;

import database.tables.pojos.Exam;
import database.tables.pojos.ExamTerm;
import database.tables.pojos.Term;
import elfak.diplomski.RoomBooker.models.ExamTermEventWrapper;

import java.util.List;

public interface ITermQuery {

    void insertTerm(Term term);

    List<Term> getTerms();

    List<Exam> getExams();

    void insertExam(ExamTerm examTerm);

    List<ExamTermEventWrapper> getExamsForTerm(Term value);
}
