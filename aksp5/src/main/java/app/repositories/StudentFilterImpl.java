package app.repositories;

import org.springframework.stereotype.Component;
import app.models.University;
import app.models.Student;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Component
public class StudentFilterImpl implements StudentFilter {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Student> findStudentsByUniversityName(String universityName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Student> cq = cb.createQuery(Student.class);

        Root<Student> student = cq.from(Student.class);
        List<Predicate> predicates = new ArrayList<>();

        if (universityName != null) {
            predicates.add(cb.equal(student.get("university").get("universityName"), universityName));
        }
        cq.orderBy(cb.asc(student.get("lastName")));
        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<Student> findStudentsByFirstNameAndLastName(String fName, String lName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Student> cq = cb.createQuery(Student.class);

        Root<Student> student = cq.from(Student.class);
        List<Predicate> predicates = new ArrayList<>();

        if (fName != null) {
            predicates.add(cb.equal(student.get("firstName"), fName));
        }
        if (lName != null) {
            predicates.add(cb.equal(student.get("lastName"), lName));
        }
        cq.orderBy(cb.asc(student.get("university").get("universityName")));
        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();
    }
}