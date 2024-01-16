package com.luv2code.springmvc.service;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class StudentAndGradeService {

    @Autowired
    private StudentDao studentDao;
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
        }
    }

    public Iterable<CollegeStudent> getGradebook() {
        Iterable<CollegeStudent> collegeStudents = studentDao.findAll();
        return collegeStudents;
    }
}
