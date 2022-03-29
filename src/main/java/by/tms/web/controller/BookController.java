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
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final SectionService sectionService;

    @Autowired
    public BookController(BookService bookService, AuthorService authorService, GenreService genreService, SectionService sectionService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
        this.sectionService = sectionService;
    }

    @ModelAttribute("allAuthors")
    public List<AuthorDto> allAuthors() {
        return authorService.findAll(Integer.MAX_VALUE, OFFSET_ZERO);
    }

    @ModelAttribute("allGenres")
    public List<GenreDto> allGenres() {
        return genreService.findAll(Integer.MAX_VALUE, OFFSET_ZERO);
    }

    @ModelAttribute("allSections")
    public List<SectionDto> allSections() {
        return sectionService.findAll(Integer.MAX_VALUE, OFFSET_ZERO);
    }

    @GetMapping("/showAllBooks")
    public String showAllBooks(@SessionAttribute(ACCOUNT_ATTRIBUTE) AccountDto account,
                               Model model,
                               @RequestParam(name = OFFSET_PARAMETER, defaultValue = "0") String offset) {
        model.addAttribute(COUNT_PAGES_ATTRIBUTE, bookService.getCountPages());
        model.addAttribute("allBooks", bookService.findAll(LIMIT_TEN, Integer.parseInt(offset)));
        if (Objects.equals("user", account.getRole())) {
            return USER_PREFIX + "allBooks";
        } else if (Objects.equals("librarian", account.getRole())) {
            return LIBRARIAN_PREFIX + "allBooks";
        }
        return REDIRECT + ERROR_PAGE;
    }

    @PostMapping("/addToCart")
    public String addToCard(@SessionAttribute(SHOPPING_CART_ATTRIBUTE) ShoppingCart shoppingCart,
                            @RequestParam("bookId") String[] bookIds,
                            Model model) {
        for (String bookId : bookIds) {
            Optional<BookDto> bookById = bookService.findById(Long.parseLong(bookId));
            if (bookById.isPresent() && bookById.get().getQuantity() != 0) {
                shoppingCart.getShoppingList().add(bookById.get());
            }
        }
        model.addAttribute(SHOPPING_CART_ATTRIBUTE, shoppingCart);
        return REDIRECT + "/showAllBooks";
    }

    @GetMapping("/editBookPage/{id}")
    public String editBookPage(@PathVariable("id") String id, Model model) {
        Optional<BookDto> bookById = bookService.findById(Long.parseLong(id));
        bookById.ifPresent(bookDto -> model.addAttribute("book", bookDto));
        return LIBRARIAN_PREFIX + "editBook";
    }

    @PostMapping("/editBook")
    public String editBook(BookDto book) {
        if (Objects.nonNull(book)) {
            bookService.update(book);
            return REDIRECT + "/showAllBooks";
        }
        return REDIRECT + ERROR_PAGE;
    }

    @GetMapping("/addBookPage")
    public String addBookPage(Model model) {
        model.addAttribute("book", BookDto.builder()
                .author(AuthorDto.builder().build())
                .genre(GenreDto.builder().build())
                .section(SectionDto.builder().build())
                .build());
        return LIBRARIAN_PREFIX + "addBook";
    }

    @PostMapping("/addBook")
    public String addBook(BookDto book) {
        if (Objects.nonNull(book)) {
            bookService.save(book);
            return REDIRECT + LIBRARIAN_PAGE;
        }
        return REDIRECT + ERROR_PAGE;
    }

    @GetMapping("/allAuthorBook/{id}")
    public String allAuthorBook(@PathVariable String id,
                                Model model,
                                @RequestParam(name = OFFSET_PARAMETER, defaultValue = "0") String offset) {
        model.addAttribute(COUNT_PAGES_ATTRIBUTE, bookService.getCountPages(Long.parseLong(id)));
        model.addAttribute("authorBook", bookService.findByAuthor(Long.parseLong(id), LIMIT_TEN, Integer.parseInt(offset)));
        return USER_PREFIX + "authorBook";
    }
}
