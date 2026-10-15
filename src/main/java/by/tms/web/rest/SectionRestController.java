package by.tms.web.rest;

import by.tms.model.dto.SectionDto;
import by.tms.model.service.SectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/sections")
@RequiredArgsConstructor
@Slf4j
public class SectionRestController {

    private final SectionService sectionService;

    @GetMapping
    public List<SectionDto> findAll() {
        return sectionService.findAll(Integer.MAX_VALUE, 0);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('librarian','admin')")
    @ResponseStatus(HttpStatus.CREATED)
    public SectionDto create(@Valid @RequestBody SectionDto section) {
        if (sectionService.findSectionByName(section.getName()).isPresent()) {
            throw new IllegalStateException("Section already exists");
        }
        section.setId(null);
        Long id = sectionService.save(section);
        log.debug("Section {} created", section.getName());
        return sectionService.findById(id).orElseThrow(() -> new NoSuchElementException("Section not found: " + id));
    }
}
