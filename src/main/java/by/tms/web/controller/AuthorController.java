package by.tms.web.controller;

import by.tms.model.dto.AuthorDto;
import by.tms.model.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @ModelAttribute("allAuthors")
    public List<AuthorDto> allAuthors() {
        return authorService.findAllAuthor(10,0);
    }

    @GetMapping("/addAuthorPage")
    public String addAuthorPage(Model model) {
        model.addAttribute("author", AuthorDto.builder().build());
        return "page/librarian/addAuthor";
    }

    @PostMapping("/addAuthor")
    public String addAuthor(AuthorDto author){
        authorService.addNewAuthor(author);
        return "redirect:/librarianPage";
    }

    @GetMapping("/allAuthorPage")
    public String allAuthorPage(){
        return "page/user/allAuthor";
    }
}
