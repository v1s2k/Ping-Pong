package app.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import app.output.StudentToUniversityRequest;
import app.models.University;
import app.models.Student;
import app.repositories.UniversityRepository;
import app.repositories.StudentRepository;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {
    private final UniversityRepository universityRepository;
    private final StudentRepository studentRepository;
    // private final EmailService emailService;

    @Transactional
    public void publish(StudentToUniversityRequest request) {
        String universityName = request.getUniversityName();
        University university = universityRepository.findByUniversityName(universityName);
        if (university == null) {
            return;
        }
        Student student = new Student();
        student.setFirstName(request.getStudentFirstName());
        student.setLastName(request.getStudentLastName());
        student.setMiddleName(request.getStudentMiddleName());
        student.setUniversity(university);
        studentRepository.save(student);

        // проверка и тест api
        log.info("*****Criteria API*****");
        List<Student> studentsByUniversityName = studentRepository.findStudentsByUniversityName("MIREA");
        log.info("-Students by university name:");
        studentsByUniversityName
                .forEach(st ->
                        log.info(st.getLastName() + " " + st.getFirstName() + " " + st.getMiddleName()
                                + " -- " + st.getUniversity().getUniversityName())
                );

        List<Student> studentsByFirstAndLastName = studentRepository
                .findStudentsByFirstNameAndLastName("Arthas", "Menetil");
        log.info("-Students by first and last name:");
        studentsByFirstAndLastName
                .forEach(st ->
                        log.info(st.getLastName() + " " + st.getFirstName() + " " + st.getMiddleName()
                                + " -- " + st.getUniversity().getUniversityName())
                );

    }

    @Transactional
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void delete(long id) {
        studentRepository.deleteById(id);
    }
}