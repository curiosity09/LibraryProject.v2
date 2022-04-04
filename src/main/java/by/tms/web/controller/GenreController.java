package by.tms.web.controller;

import by.tms.model.dto.GenreDto;
import by.tms.model.service.GenreService;
import by.tms.web.util.LoggerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import static by.tms.web.util.PageUtil.*;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/librarian/addGenrePage")
    public String addGenrePage(Model model) {
        model.addAttribute(GENRE_ATTRIBUTE, GenreDto.builder().build());
        return LIBRARIAN_PREFIX + ADD_GENRE_SUFFIX;
    }

    @PostMapping("/addGenre")
    public String addGenre(GenreDto genre) {
        genreService.save(genre);
        log.debug(LoggerUtil.ENTITY_WAS_SAVED_IN_CONTROLLER, genre);
        return REDIRECT + LIBRARIAN_PAGE;
    }
}
