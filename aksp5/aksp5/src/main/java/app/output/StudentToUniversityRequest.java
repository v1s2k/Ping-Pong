package app.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
@AllArgsConstructor
public class StudentToUniversityRequest {
    @NotBlank
    private final String studentFirstName;
    @NotBlank
    private final String studentLastName;
    private final String studentMiddleName;
    @NotBlank
    private final String universityName;
}