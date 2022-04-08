package by.tms.web.controller;

import by.tms.model.dto.AuthorDto;
import by.tms.model.dto.BookDto;
import by.tms.model.dto.GenreDto;
import by.tms.model.dto.SectionDto;
import by.tms.model.dto.ShoppingCart;
import by.tms.model.dto.user.AccountDto;
import by.tms.model.service.AuthorService;
import by.tms.model.service.BookService;
import by.tms.model.service.GenreService;
import by.tms.model.service.SectionService;
import by.tms.web.util.LoggerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static by.tms.web.util.PageUtil.*;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final SectionService sectionService;

    @ModelAttribute(ALL_AUTHORS_ATTRIBUTE)
    public List<AuthorDto> allAuthors() {
        return authorService.findAll(Integer.MAX_VALUE, OFFSET_ZERO);
    }

    @ModelAttribute(ALL_GENRES_ATTRIBUTE)
    public List<GenreDto> allGenres() {
        return genreService.findAll(Integer.MAX_VALUE, OFFSET_ZERO);
    }

    @ModelAttribute(ALL_SECTIONS_ATTRIBUTE)
    public List<SectionDto> allSections() {
        return sectionService.findAll(Integer.MAX_VALUE, OFFSET_ZERO);
    }

    @GetMapping({"/librarian/showAllBooks", "/user/showAllBooks"})
    public String showAllBooks(@SessionAttribute(ACCOUNT_ATTRIBUTE) AccountDto account,
                               Model model,
                               @RequestParam(name = OFFSET_PARAMETER, defaultValue = VALUE_ZERO) String offset) {
        model.addAttribute(COUNT_PAGES_ATTRIBUTE, bookService.getCountPages());
        model.addAttribute(ALL_BOOKS_ATTRIBUTE, bookService.findAll(LIMIT_TEN, Integer.parseInt(offset)));
        switch (account.getRole()) {
            case USER_ROLE:
                return USER_PREFIX + ALL_BOOKS_PAGE;
            case LIBRARIAN_ROLE:
                return LIBRARIAN_PREFIX + ALL_BOOKS_PAGE;
            default:
                return REDIRECT + ERROR_PAGE;
        }
    }

    @PostMapping("/addToCart")
    public String addToCard(@SessionAttribute(SHOPPING_CART_ATTRIBUTE) ShoppingCart shoppingCart,
                            @RequestParam(BOOK_ID_PARAMETER) String[] bookIds,
                            Model model) {
        for (String bookId : bookIds) {
            Optional<BookDto> bookById = bookService.findById(Long.parseLong(bookId));
            if (bookById.isPresent() && bookById.get().getQuantity() != 0) {
                log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_CONTROLLER_BY, bookById.get(), bookId);
                shoppingCart.getShoppingList().add(bookById.get());
            }
        }
        model.addAttribute(SHOPPING_CART_ATTRIBUTE, shoppingCart);
        return REDIRECT + SHOW_ALL_BOOKS_USER_PAGE;
    }

    @GetMapping("/librarian/editBookPage/{id}")
    public String editBookPage(@PathVariable(ID_PATH_VARIABLE) String id, Model model) {
        Optional<BookDto> bookById = bookService.findById(Long.parseLong(id));
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_CONTROLLER_BY, bookById, id);
        bookById.ifPresent(bookDto -> model.addAttribute(BOOK_ATTRIBUTE, bookDto));
        return LIBRARIAN_PREFIX + EDIT_BOOK_SUFFIX;
    }

    @PostMapping("/editBook")
    public String editBook(BookDto book) {
        if (Objects.nonNull(book)) {
            bookService.update(book);
            log.debug(LoggerUtil.ENTITY_WAS_UPDATED_IN_CONTROLLER, book);
            return REDIRECT + SHOW_ALL_BOOKS_LIB_PAGE;
        }
        return REDIRECT + ERROR_PAGE;
    }

    @GetMapping("/librarian/addBookPage")
    public String addBookPage(Model model) {
        model.addAttribute(BOOK_ATTRIBUTE, BookDto.builder()
                .author(AuthorDto.builder().build())
                .genre(GenreDto.builder().build())
                .section(SectionDto.builder().build())
                .build());
        return LIBRARIAN_PREFIX + ADD_BOOK_SUFFIX;
    }

    @PostMapping("/addBook")
    public String addBook(BookDto book) {
        if (Objects.nonNull(book)) {
            bookService.save(book);
            log.debug(LoggerUtil.ENTITY_WAS_SAVED_IN_CONTROLLER, book);
            return REDIRECT + LIBRARIAN_PAGE;
        }
        return REDIRECT + ERROR_PAGE;
    }

    @GetMapping("/user/allAuthorBook/{id}")
    public String allAuthorBook(@PathVariable String id,
                                Model model,
                                @RequestParam(name = OFFSET_PARAMETER, defaultValue = VALUE_ZERO) String offset) {
        model.addAttribute(COUNT_PAGES_ATTRIBUTE, bookService.getCountPages(Long.parseLong(id)));
        model.addAttribute(AUTHOR_BOOK_ATTRIBUTE, bookService.findByAuthor(Long.parseLong(id),
                LIMIT_TEN, Integer.parseInt(offset)));
        return USER_PREFIX + AUTHOR_BOOK_SUFFIX;
    }
}
