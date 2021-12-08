package app.repositories;

import app.models.Student;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface StudentFilter {
    List<Student> findStudentsByUniversityName(String universityName);
    List<Student> findStudentsByFirstNameAndLastName(String fName, String lName);
}