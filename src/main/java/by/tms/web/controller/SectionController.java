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

@Controller
public class SectionController {

    private final SectionService sectionService;

    @Autowired
    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @ModelAttribute("allSections")
    public List<SectionDto> allSections() {
        return sectionService.findAllSection(10,0);
    }

    @GetMapping("/addSectionPage")
    public String addSectionPage(Model model) {
        model.addAttribute("section", SectionDto.builder().build());
        return "page/librarian/addSection";
    }

    @PostMapping("/addSection")
    public String addSection(SectionDto section){
        sectionService.addNewSection(section);
        return "redirect:/librarianPage";
    }
}
