package by.tms.web.controller;

import by.tms.model.dto.GenreDto;
import by.tms.model.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @ModelAttribute("allGenres")
    public List<GenreDto> allGenres() {
        return genreService.findAllGenre(10,0);
    }

    @GetMapping("/addGenrePage")
    public String addGenrePage(Model model) {
        model.addAttribute("genre", GenreDto.builder().build());
        return "page/librarian/addGenre";
    }

    @PostMapping("/addGenre")
    public String addGenre(GenreDto genre){
        genreService.addNewGenre(genre);
        return "redirect:/librarianPage";
    }
}
