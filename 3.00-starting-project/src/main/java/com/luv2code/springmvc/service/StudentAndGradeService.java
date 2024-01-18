package com.luv2code.springmvc.service;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentAndGradeService {

    @Autowired
    private StudentDao studentDao;

    @Autowired
    @Qualifier("mathGrades")
    private MathGrade mathGrade;

    @Autowired
    @Qualifier("scienceGrades")
    private ScienceGrade scienceGrade;

    @Autowired
    @Qualifier("historyGrades")
    private HistoryGrade historyGrade;

    @Autowired
    private MathGradesDao mathGradesDao;

    @Autowired
    private ScienceGradesDao scienceGradesDao;

    @Autowired
    private HistoryGradesDao historyGradesDao;

    @Autowired
    private StudentGrades studentGrades;

    public void createStudent(String fname, String lname, String email) {
        CollegeStudent student = new CollegeStudent(fname, lname, email);
        student.setId(0);
        studentDao.save(student);
    }

    public boolean checkIfStudentIsNull(int i) {
        Optional<CollegeStudent> student = studentDao.findById(i);
        if(student.isPresent()) {
            return true;
        }
        return false;
    }

    public void deleteStudent(int i) {
        if(checkIfStudentIsNull(i)) {
            studentDao.deleteById(i);
            mathGradesDao.deleteByStudentId(i);
            scienceGradesDao.deleteByStudentId(i);
            historyGradesDao.deleteByStudentId(i);
        }
    }

    public Iterable<CollegeStudent> getGradebook() {
        Iterable<CollegeStudent> collegeStudents = studentDao.findAll();
        return collegeStudents;
    }

    public boolean createGrade(double grade, int id, String gradeType) {
        if(!checkIfStudentIsNull(id)) {
            return false;
        }

        if(grade >= 0 && grade <= 100) {
            if(gradeType.equals("math")) {
                mathGrade.setId(0);
                mathGrade.setGrade(grade);
                mathGrade.setStudentId(id);
                mathGradesDao.save(mathGrade);
                return true;
            }
            if(gradeType.equals("science")) {
                scienceGrade.setId(0);
                scienceGrade.setGrade(grade);
                scienceGrade.setStudentId(id);
                scienceGradesDao.save(scienceGrade);
                return true;
            }
            if(gradeType.equals("history")) {
                historyGrade.setId(0);
                historyGrade.setGrade(grade);
                historyGrade.setStudentId(id);
                historyGradesDao.save(historyGrade);
                return true;
            }
        }

        return false;
    }

    public int deleteGrade(int id, String gradeType) {
        int studentId=0;

        if(gradeType.equals("math")) {
            Optional<MathGrade> grade = mathGradesDao.findById(id);
            if(!grade.isPresent()) {
                return studentId;
            }
            studentId = grade.get().getStudentId();
            mathGradesDao.deleteById(id);
        }
        if(gradeType.equals("science")) {
            Optional<ScienceGrade> grade = scienceGradesDao.findById(id);
            if(!grade.isPresent()) {
                return studentId;
            }
            studentId = grade.get().getStudentId();
            scienceGradesDao.deleteById(id);
        }
        if(gradeType.equals("history")) {
            Optional<HistoryGrade> grade = historyGradesDao.findById(id);
            if(!grade.isPresent()) {
                return studentId;
            }
            studentId = grade.get().getStudentId();
            historyGradesDao.deleteById(id);
        }

        return studentId;
    }

    public GradebookCollegeStudent studentInformation(int i) {

        if(!checkIfStudentIsNull(i)) {
            return null;
        }

        Optional<CollegeStudent> student = studentDao.findById(i);
        Iterable<MathGrade> mathGrades = mathGradesDao.findGradeByStudentId(i);
        Iterable<ScienceGrade> scienceGrades = scienceGradesDao.findGradeByStudentId(i);
        Iterable<HistoryGrade> historyGrades = historyGradesDao.findGradeByStudentId(i);

        List<Grade> mathGradesList = new ArrayList<>();
        mathGrades.forEach(mathGradesList::add);

        List<Grade> scienceGradesList = new ArrayList<>();
        scienceGrades.forEach(scienceGradesList::add);

        List<Grade> historyGradesList = new ArrayList<>();
        historyGrades.forEach(historyGradesList::add);

        studentGrades.setMathGradeResults(mathGradesList);
        studentGrades.setScienceGradeResults(scienceGradesList);
        studentGrades.setHistoryGradeResults(historyGradesList);

        GradebookCollegeStudent gradebookCollegeStudent = new GradebookCollegeStudent(
                student.get().getId(),
                student.get().getFirstname(),
                student.get().getLastname(),
                student.get().getEmailAddress(),
                studentGrades);

        return gradebookCollegeStudent;
    }

    public void configureStudentInformationModel(int id, Model m) {

        GradebookCollegeStudent studentEntity = studentInformation(id);

        m.addAttribute("student", studentEntity);
        if(studentEntity.getStudentGrades().getMathGradeResults().size() > 0) {
            m.addAttribute("mathAverage", studentEntity.getStudentGrades().findGradePointAverage(
                    studentEntity.getStudentGrades().getMathGradeResults()
            ));
        } else {
            m.addAttribute("mathAverage", "N/A");
        }

        if(studentEntity.getStudentGrades().getScienceGradeResults().size() > 0) {
            m.addAttribute("scienceAverage", studentEntity.getStudentGrades().findGradePointAverage(
                    studentEntity.getStudentGrades().getScienceGradeResults()
            ));
        } else {
            m.addAttribute("scienceAverage", "N/A");
        }

        if(studentEntity.getStudentGrades().getHistoryGradeResults().size() > 0) {
            m.addAttribute("historyAverage", studentEntity.getStudentGrades().findGradePointAverage(
                    studentEntity.getStudentGrades().getHistoryGradeResults()
            ));
        } else {
            m.addAttribute("historyAverage", "N/A");
        }
    }
}
