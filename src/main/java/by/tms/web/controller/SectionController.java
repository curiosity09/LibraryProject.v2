package by.tms.web.controller;

import by.tms.model.dto.SectionDto;
import by.tms.model.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import static by.tms.web.util.PageUtil.*;

@Controller
public class SectionController {

    private final SectionService sectionService;

    @Autowired
    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @ModelAttribute("allSections")
    public List<SectionDto> allSections() {
        return sectionService.findAll(LIMIT_TEN, OFFSET_ZERO);
    }

    @GetMapping("/addSectionPage")
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
