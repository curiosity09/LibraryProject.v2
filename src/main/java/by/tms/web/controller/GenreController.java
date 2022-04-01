package by.tms.web.controller;

import by.tms.model.dto.GenreDto;
import by.tms.model.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import static by.tms.web.util.PageUtil.*;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/librarian/addGenrePage")
    public String addGenrePage(Model model) {
        model.addAttribute("genre", GenreDto.builder().build());
        return LIBRARIAN_PREFIX + "addGenre";
    }

    @PostMapping("/addGenre")
    public String addGenre(GenreDto genre) {
        genreService.save(genre);
        return REDIRECT + LIBRARIAN_PAGE;
    }
}
