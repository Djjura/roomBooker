package elfak.diplomski.RoomBooker.query.impl;

import database.Tables;
import database.tables.pojos.Exam;
import database.tables.pojos.ExamTerm;
import database.tables.pojos.Term;
import database.tables.records.ExamTermRecord;
import database.tables.records.TermRecord;
import elfak.diplomski.RoomBooker.models.ExamTermEventWrapper;
import elfak.diplomski.RoomBooker.query.ITermQuery;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(propagation = Propagation.MANDATORY)
public class TermQuery implements ITermQuery {
    @Autowired
    private DSLContext dsl;

    private static final database.tables.Term TERM = Tables.TERM;
    private static final database.tables.Exam EXAM = Tables.EXAM;
    private static final database.tables.ExamTerm EXAM_TERM = Tables.EXAM_TERM;

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void insertTerm(Term term) {
        UUID uuid = UUID.randomUUID();
        term.setUuid(uuid.toString());
        TermRecord termRecord = dsl.newRecord(TERM, term);
        dsl.insertInto(TERM).set(termRecord).execute();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Term> getTerms() {
        return dsl.selectFrom(TERM).fetchInto(Term.class);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Exam> getExams() {
        return dsl.selectFrom(EXAM).fetchInto(Exam.class);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void insertExam(ExamTerm examTerm) {
        UUID uuid = UUID.randomUUID();
        examTerm.setUuid(uuid.toString());
        ExamTermRecord examTermRecord = dsl.newRecord(EXAM_TERM, examTerm);
        dsl.insertInto(EXAM_TERM).set(examTermRecord).execute();
    }

    @Override
    public List<ExamTermEventWrapper> getExamsForTerm(Term term) {
        List<ExamTermEventWrapper> examTermEventWrappers = dsl.select(EXAM_TERM.UUID, EXAM_TERM.EXAM_ID, EXAM_TERM.TERM_ID, EXAM_TERM.DATE, EXAM_TERM.STUDENTS_APPLIED, EXAM.NAME)
                .from(EXAM_TERM)
                .join(EXAM).on(EXAM_TERM.EXAM_ID.eq(EXAM.ID))
                .where(TERM.UUID.eq(term.getUuid()))
                .fetchInto(ExamTermEventWrapper.class);

        return examTermEventWrappers;
    }
}
