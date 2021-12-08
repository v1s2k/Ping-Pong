package app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import app.output.Outputer;
import app.services.UniversityService;

import java.util.Map;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {
    private final UniversityService universityService;
    private final Outputer outputer;

    @GetMapping
    public String index(Map<String, Object> model) {
        model.put(
                "universities",
                universityService.takeAllUniversities(outputer::toUniversityResponseList)
        );
        return "home";
    }
}