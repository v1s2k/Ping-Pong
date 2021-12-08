package app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import app.output.Outputer;
import app.services.UniversityService;

import java.util.Map;

@Controller
@RequestMapping("/university")
@RequiredArgsConstructor
public class UniversityController {
    private final Outputer outputer;
    private final UniversityService universityService;

    @GetMapping
    public String getUniversities(Map<String, Object> model) {
        model.put(
                "universities",
                universityService.takeAllUniversities(outputer::toUniversityResponseList)
        );
        return "university";
    }

    @PostMapping("{universityId}")
    public String getUniversity(
            @PathVariable long universityId,
            Map<String, Object> model
    ) {
        model.put(
                "university",
                universityService.takeUniversityById(universityId, outputer::toUniversityResponse)
        );
        return "university";
    }

    @PostMapping("{universityId}/delete")
    public RedirectView delete(@PathVariable long universityId) {
        universityService.delete(universityId);
        return new RedirectView("/home");
    }

    @GetMapping("add")
    public String add() {
        return "add";
    }
}