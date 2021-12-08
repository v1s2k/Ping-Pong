package app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import app.models.University;
import app.models.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, StudentFilter {
    void deleteAllByUniversity(University university);
}