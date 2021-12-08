package  app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import app.output.StudentToUniversityRequest;
import app.services.UniversityService;
import app.services.StudentService;

import javax.validation.Valid;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final UniversityService universityService;
    private final StudentService studentService;

    @PostMapping("add")
    public RedirectView add(
            @Valid @ModelAttribute("addStudent") StudentToUniversityRequest studentToUniversityRequest,
            BindingResult result
    ) {
        if (!result.hasErrors()) {
            universityService.publish(studentToUniversityRequest);
            studentService.publish(studentToUniversityRequest);
        }
        return new RedirectView("/home");
    }

    @PostMapping("{studentId}/delete")
    public RedirectView delete(@PathVariable long studentId) {
        studentService.delete(studentId);
        return new RedirectView("/home");
    }
}