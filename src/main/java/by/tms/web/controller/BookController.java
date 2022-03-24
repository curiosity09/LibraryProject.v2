package by.tms.web.controller;

import by.tms.model.dto.AuthorDto;
import by.tms.model.dto.BookDto;
import by.tms.model.dto.GenreDto;
import by.tms.model.dto.SectionDto;
import by.tms.model.dto.ShoppingCart;
import by.tms.model.dto.user.AccountDto;
import by.tms.model.entity.Book;
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
        return authorService.findAllAuthor(10, 0);
    }

    @ModelAttribute("allGenres")
    public List<GenreDto> allGenres() {
        return genreService.findAllGenre(10, 0);
    }

    @ModelAttribute("allSections")
    public List<SectionDto> allSections() {
        return sectionService.findAllSection(10, 0);
    }

    @GetMapping("/showAllBooks")
    public String showAllBooks(@SessionAttribute("account") AccountDto account,
                               Model model,
                               @RequestParam(name = "offset", defaultValue = "0") String offset) {
        model.addAttribute("allBooks", bookService.findAllBook(10, Integer.parseInt(offset)));
        if (Objects.equals("user", account.getRole())) {
            return "page/user/allBooks";
        } else if (Objects.equals("librarian", account.getRole())) {
            return "page/librarian/allBooks";
        }
        return "redirect:/errorPage";
    }

/*    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        model.addAttribute("book", bookService.findBookById(id));
        return "page/user/allBooks";
    }*/

    @PostMapping("/addToCart")
    public String addToCard(@SessionAttribute("shoppingCart") ShoppingCart shoppingCart,
                            @RequestParam("bookId") String[] bookIds,
                            Model model) {
        for (String bookId : bookIds) {
            Optional<BookDto> bookById = bookService.findBookById(Long.parseLong(bookId));
            if (bookById.isPresent() && bookById.get().getQuantity() != 0) {
                shoppingCart.getShoppingList().add(bookById.get());
            }
        }
        model.addAttribute("shoppingCart", shoppingCart);
        return "redirect:/showAllBooks";
    }

    @GetMapping("/editBookPage/{id}")
    public String editBookPage(@PathVariable("id") String id, Model model) {
        Optional<BookDto> bookById = bookService.findBookById(Long.parseLong(id));
        bookById.ifPresent(bookDto -> model.addAttribute("book", bookDto));
        return "page/librarian/editBook";
    }

    @PostMapping("/editBook")
    public String editBook(BookDto book) {
        if (Objects.nonNull(book)) {
            bookService.updateBook(book);
            return "redirect:/showAllBooks";
        }
        return "redirect:/errorPage";
    }

    @GetMapping("/addBookPage")
    public String addBookPage(Model model) {
        model.addAttribute("book", BookDto.builder()
                .author(AuthorDto.builder().build())
                .genre(GenreDto.builder().build())
                .section(SectionDto.builder().build())
                .build());
        return "page/librarian/addBook";
    }

    @PostMapping("/addBook")
    public String addBook(BookDto book) {
        if (Objects.nonNull(book)) {
            bookService.addNewBook(book);
            return "redirect:/librarianPage";
        }
        return "redirect:/errorPage";
    }

    @GetMapping("/allAuthorBook/{id}")
    public String allAuthorBook(@PathVariable String id, Model model) {
        model.addAttribute("authorBook", bookService.findByAuthor(Long.parseLong(id), 10, 0));
        return "page/user/authorBook";
    }
}