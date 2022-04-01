package by.tms.web.controller;

import by.tms.model.dto.SectionDto;
import by.tms.model.service.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import static by.tms.web.util.PageUtil.*;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SectionController {

    private final SectionService sectionService;

    @GetMapping("/librarian/addSectionPage")
    public String addSectionPage(Model model) {
        model.addAttribute("section", SectionDto.builder().build());
        return LIBRARIAN_PREFIX + "addSection";
    }

    @PostMapping("/addSection")
    public String addSection(SectionDto section) {
        sectionService.save(section);
        return REDIRECT + LIBRARIAN_PAGE;
    }
}
