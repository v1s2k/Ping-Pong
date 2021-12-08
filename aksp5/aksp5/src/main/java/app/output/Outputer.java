package app.output;

import org.springframework.stereotype.Component;
import app.models.University;
import app.models.Student;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Outputer {
    public List<StudentResponse> toStudentResponseList(List<Student> students) {
        return students.stream().map(student -> {
            StudentResponse response = new StudentResponse();
            response.setId(student.getId());
            response.setFirstName(student.getFirstName());
            response.setLastName(student.getLastName());
            response.setMiddleName(student.getMiddleName());
            return response;
        }).collect(Collectors.toList());
    }

    public UniversityResponse toUniversityResponse(University university) {
        UniversityResponse response = new UniversityResponse();
        response.setId(university.getId());
        response.setUniversityName(university.getUniversityName());
        response.setCreationDate(university.getCreationDate());
        response.setStudents(toStudentResponseList(university.getStudents()));
        return response;
    }

    public List<UniversityResponse> toUniversityResponseList(List<University> universities) {
        return universities.stream()
                .map(this::toUniversityResponse)
                .collect(Collectors.toList());
    }
}