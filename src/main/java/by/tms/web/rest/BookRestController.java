package by.tms.web.rest;

import by.tms.model.dto.BookDto;
import by.tms.model.service.BookService;
import by.tms.web.rest.dto.PageResponse;
import by.tms.web.rest.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Slf4j
public class BookRestController {

    private final BookService bookService;

    @GetMapping
    public PageResponse<BookDto> findAll(@RequestParam(defaultValue = "0") int page) {
        int normalized = PagingUtils.normalizePage(page);
        List<BookDto> content = bookService.findAll(PagingUtils.PAGE_SIZE, PagingUtils.offset(normalized));
        return PageResponse.of(content, normalized, PagingUtils.PAGE_SIZE,
                PagingUtils.totalPages(bookService.getCountPages()));
    }

    @GetMapping("/{id}")
    public BookDto findById(@PathVariable Long id) {
        return bookService.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book not found: " + id));
    }

    @GetMapping("/by-author/{authorId}")
    public PageResponse<BookDto> findByAuthor(@PathVariable Long authorId, @RequestParam(defaultValue = "0") int page) {
        int normalized = PagingUtils.normalizePage(page);
        List<BookDto> content = bookService.findByAuthor(authorId, PagingUtils.PAGE_SIZE, PagingUtils.offset(normalized));
        return PageResponse.of(content, normalized, PagingUtils.PAGE_SIZE,
                PagingUtils.totalPages(bookService.getCountPages(authorId)));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('librarian','admin')")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto create(@Valid @RequestBody BookDto book) {
        book.setId(null);
        Long id = bookService.save(book);
        log.debug("Book {} created", book.getName());
        return bookService.findById(id).orElseThrow(() -> new NoSuchElementException("Book not found: " + id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('librarian','admin')")
    public BookDto update(@PathVariable Long id, @Valid @RequestBody BookDto book) {
        book.setId(id);
        bookService.update(book);
        log.debug("Book {} updated", id);
        return bookService.findById(id).orElseThrow(() -> new NoSuchElementException("Book not found: " + id));
    }
}
