package elfak.diplomski.RoomBooker.service;

import database.tables.pojos.Exam;
import database.tables.pojos.Term;
import elfak.diplomski.RoomBooker.models.ExamDates;
import elfak.diplomski.RoomBooker.models.ExamTermEventWrapper;
import elfak.diplomski.RoomBooker.query.impl.TermQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExamSchedulerService {

    @Autowired
    private TermQuery termQuery;

    //PRAVILA:
    //ispiti i istog semestra treba da budu sto dalje jedan od drugog
    //zatim iz iste godine takodje
    //Treba da se zakazu u sobama po broju studenata
    public void generateTermShedule(Term term) {
        LocalDate startDate = term.getStartDate();
        LocalDate endDate = term.getEndDate();
        List<Exam> termExams = termQuery.getTermExams(term.getUuid());
        List<ExamTermEventWrapper> examsForTerm = termQuery.getExamsForTerm(term);

        int numberOFDaysForExam = (int) ChronoUnit.DAYS.between(startDate, endDate);
        HashMap<Integer, List<Exam>> examsMap = new HashMap<>();
        List<LocalDate> dates = startDate.datesUntil(endDate).filter(localDate -> {
            return localDate.getDayOfWeek() != DayOfWeek.SUNDAY;
        }).collect(Collectors.toList());

        List<ExamDates> examDates = new ArrayList<>();
        for (LocalDate date : dates) {
            examDates.add(new ExamDates(date));
        }

        for (Exam el : termExams) {
            List<Exam> semestarExams = examsMap.get(el.getSemester());
            if (semestarExams == null) {
                semestarExams = new ArrayList<>();
            }
            semestarExams.add(el);
            examsMap.put(el.getSemester(), semestarExams);
        }
        for (Map.Entry<Integer, List<Exam>> el : examsMap.entrySet()) {
            List<Exam> exams = el.getValue();
            int offset = 0;
            int index = el.getKey() - 1;
            if (numberOFDaysForExam > exams.size()) {
                offset = numberOFDaysForExam / exams.size();
            }
            for (Exam exam : exams) {
                boolean dateFound = false;
                while (dateFound == false) {
                    if (index > examDates.size() - 1) {
                        index = index % examDates.size();
                    }
                    ExamDates date = examDates.get(index);
                    for (Exam e : date.getExamLIst()) {
                        if (e.getSemester() == exam.getSemester()) {
                            offset = offset / 2;
                            index += offset;
                            break;
                        }
                    }
                    date.getExamLIst().add(exam);
                    dateFound = true;
                }
                for (ExamTermEventWrapper examTermEventWrapper : examsForTerm) {
                    if (examTermEventWrapper.getExamTerm().getExamId().equals(exam.getId())) {
                        ExamDates date = examDates.get(index);
                        examTermEventWrapper.getExamTerm().setDate(date.getExamDay());

                    }
                }
                index += offset;
            }
        }

        termQuery.updateExemsInTerm(examsForTerm);

    }
}
