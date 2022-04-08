package by.tms.web.controller;

import by.tms.model.dto.SectionDto;
import by.tms.model.service.SectionService;
import by.tms.web.util.LoggerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

import java.util.Optional;

import static by.tms.web.util.PageUtil.*;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class SectionController {

    private final SectionService sectionService;

    @GetMapping("/librarian/addSectionPage")
    public String addSectionPage(Model model) {
        model.addAttribute(SECTION_ATTRIBUTE, SectionDto.builder().build());
        return LIBRARIAN_PREFIX + ADD_SECTION_SUFFIX;
    }

    @PostMapping("/addSection")
    public String addSection(@ModelAttribute(SECTION_ATTRIBUTE) @Valid SectionDto section, Errors errors) {
        if (errors.hasErrors()) {
            return LIBRARIAN_PREFIX + ADD_SECTION_SUFFIX;
        }
        Optional<SectionDto> sectionByName = sectionService.findSectionByName(section.getName());
        if (sectionByName.isEmpty()) {
            sectionService.save(section);
            log.debug(LoggerUtil.ENTITY_WAS_SAVED_IN_CONTROLLER, section);
            return REDIRECT + LIBRARIAN_PAGE;
        }
        return REDIRECT + ERROR_PAGE;
    }
}
