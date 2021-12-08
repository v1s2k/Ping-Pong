package app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import app.models.University;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {
    University findByUniversityName(String universityName);
}