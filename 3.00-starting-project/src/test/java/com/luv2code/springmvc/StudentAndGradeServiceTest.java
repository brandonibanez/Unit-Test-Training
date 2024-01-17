package com.luv2code.springmvc;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application.properties")
@SpringBootTest
public class StudentAndGradeServiceTest {

    @Autowired
    private StudentAndGradeService studentService;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private MathGradesDao mathGradesDao;

    @Autowired
    private ScienceGradesDao scienceGradesDao;

    @Autowired
    private HistoryGradesDao historyGradesDao;

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute("insert into student(id, firstname, lastname, email_address)" +
                "values (1, 'Rabby', 'Dabby', 'test@test.com')");

        jdbc.execute("insert into math_grade(id, student_id, grade)" +
                "values (1, 1, 100.00)");

        jdbc.execute("insert into science_grade(id, student_id, grade)" +
                "values (1, 1, 100.00)");

        jdbc.execute("insert into history_grade(id, student_id, grade)" +
                "values (1, 1, 100.00)");
    }

    @Test
    public void createStudentService() {

        studentService.createStudent("Rabby", "Dabby", "dabbydab@test.com");

        CollegeStudent student = studentDao.findByEmailAddress("dabbydab@test.com");

        assertEquals("dabbydab@test.com", student.getEmailAddress(), "find by email");
    }

    @Test
    public void isStudentNullCheck() {
        assertTrue(studentService.checkIfStudentIsNull(1));

        assertFalse(studentService.checkIfStudentIsNull(0));
    }

    @Test
    public void deleteStudentService() {
        Optional<CollegeStudent> deleteCollegeStudent = studentDao.findById(1);
        Optional<MathGrade> deletedMathGrade = mathGradesDao.findById(1);
        Optional<ScienceGrade> deletedScienceGrade = scienceGradesDao.findById(1);
        Optional<HistoryGrade> deletedHistoryGrade = historyGradesDao.findById(1);

        assertTrue(deleteCollegeStudent.isPresent(), "Return True");
        assertTrue(deletedMathGrade.isPresent(), "Return True");
        assertTrue(deletedScienceGrade.isPresent(), "Return True");
        assertTrue(deletedHistoryGrade.isPresent(), "Return True");

        studentService.deleteStudent(1);

        deleteCollegeStudent = studentDao.findById(1);
        deletedMathGrade = mathGradesDao.findById(1);
        deletedScienceGrade = scienceGradesDao.findById(1);
        deletedHistoryGrade = historyGradesDao.findById(1);


        assertFalse(deleteCollegeStudent.isPresent(), "Return False");
        assertFalse(deletedMathGrade.isPresent(), "Return False");
        assertFalse(deletedScienceGrade.isPresent(), "Return False");
        assertFalse(deletedHistoryGrade.isPresent(), "Return False");
    }

    @Sql("/insertData.sql")
    @Test
    public void getGradebookService() {
        Iterable<CollegeStudent> iterableCollegeStudents = studentService.getGradebook();

        List<CollegeStudent> collegeStudents = new ArrayList<>();

        for(CollegeStudent collegeStudent : iterableCollegeStudents) {
            collegeStudents.add(collegeStudent);
        }

        assertEquals(5, collegeStudents.size());
    }

    @Test
    public void createGradeService() {

        assertTrue(studentService.createGrade(80.50,1,"math"));
        assertTrue(studentService.createGrade(80.50,1,"science"));
        assertTrue(studentService.createGrade(80.50,1,"history"));

        Iterable<MathGrade> mathGrades = mathGradesDao.findGradeByStudentId(1);
        Iterable<ScienceGrade> scienceGrades = scienceGradesDao.findGradeByStudentId(1);
        Iterable<HistoryGrade> historyGrades = historyGradesDao.findGradeByStudentId(1);

        assertTrue( ((Collection<MathGrade>) mathGrades).size() == 2, "Student has math grades");
        assertTrue(((Collection<ScienceGrade>) scienceGrades).size() == 2, "Student has science grades");
        assertTrue(((Collection<HistoryGrade>) historyGrades).size() == 2, "Student has history grades");
    }

    @Test
    public void createGradeServiceReturnFalse() {
        assertFalse(studentService.createGrade(105,1,"math"));
        assertFalse(studentService.createGrade(-5,1,"math"));
        assertFalse(studentService.createGrade(80.50,2,"math"));
        assertFalse(studentService.createGrade(80.50,1,"literature"));
    }

    @Test
    public void deleteGradeService() {
        assertEquals(1, studentService.deleteGrade(1,"math"),
                "Returns student id after delete");
        assertEquals(1, studentService.deleteGrade(1,"science"),
                "Returns student id after delete");
        assertEquals(1, studentService.deleteGrade(1,"history"),
                "Returns student id after delete");
    }

    @Test
    public void deleteGradeServiceReturnStudentIdOfZero() {
        assertEquals(0, studentService.deleteGrade(0,"science"),
                "No student should have 0 id");
        assertEquals(0, studentService.deleteGrade(1,"literature"),
                "No student should have 0 id");
    }

    @Test
    public void studentInformation() {

        GradebookCollegeStudent gradebookCollegeStudent = studentService.studentInformation(1);

        assertNotNull(gradebookCollegeStudent);
        assertEquals(1,gradebookCollegeStudent.getId());
        assertEquals("Rabby", gradebookCollegeStudent.getFirstname());
        assertEquals("Dabby", gradebookCollegeStudent.getLastname());
        assertEquals("test@test.com", gradebookCollegeStudent.getEmailAddress());
        assertTrue(gradebookCollegeStudent.getStudentGrades().getMathGradeResults().size() == 1);
        assertTrue(gradebookCollegeStudent.getStudentGrades().getScienceGradeResults().size() == 1);
        assertTrue(gradebookCollegeStudent.getStudentGrades().getHistoryGradeResults().size() == 1);
    }

    @Test
    public void studentInformationServiceReturnNull() {

        GradebookCollegeStudent gradebookCollegeStudent = studentService.studentInformation(0);

        assertNull(gradebookCollegeStudent);
    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute("DELETE FROM student");
        jdbc.execute("DELETE FROM math_grade");
        jdbc.execute("DELETE FROM science_grade");
        jdbc.execute("DELETE FROM history_grade");
    }
}
