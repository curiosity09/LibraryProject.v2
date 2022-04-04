package by.tms.web.controller;

import by.tms.model.dto.AuthorDto;
import by.tms.model.service.AuthorService;
import by.tms.web.util.LoggerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static by.tms.web.util.PageUtil.*;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/librarian/addAuthorPage")
    public String addAuthorPage(Model model) {
        model.addAttribute(AUTHOR_ATTRIBUTE, AuthorDto.builder().build());
        return LIBRARIAN_PREFIX + ADD_AUTHOR_SUFFIX;
    }

    @PostMapping("/librarian/addAuthor")
    public String addAuthor(AuthorDto author) {
        authorService.save(author);
        log.debug(LoggerUtil.ENTITY_WAS_SAVED_IN_CONTROLLER, author);
        return REDIRECT + LIBRARIAN_PAGE;
    }

    @GetMapping("/user/allAuthorPage")
    public String allAuthorPage(Model model,
                                @RequestParam(name = OFFSET_PARAMETER, defaultValue = VALUE_ZERO) String offset) {
        model.addAttribute(COUNT_PAGES_ATTRIBUTE, authorService.getCountPages());
        model.addAttribute(ALL_AUTHORS_ATTRIBUTE, authorService.findAll(LIMIT_TEN, Integer.parseInt(offset)));
        return USER_PREFIX + ALL_AUTHOR_SUFFIX;
    }
}
