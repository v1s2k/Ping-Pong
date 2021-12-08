package app.output;

import com.sun.istack.NotNull;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Setter
@ToString
public class UniversityResponse {
    @NotNull
    private Long id;
    @NotBlank
    private String universityName;
    @NotBlank
    private String creationDate;
    private List<StudentResponse> students;
}