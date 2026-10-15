package by.tms.web.rest;

import by.tms.model.dto.AuthorDto;
import by.tms.model.service.AuthorService;
import by.tms.web.rest.dto.PageResponse;
import by.tms.web.rest.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
@Slf4j
public class AuthorRestController {

    private final AuthorService authorService;

    @GetMapping
    public PageResponse<AuthorDto> findAll(@RequestParam(defaultValue = "0") int page) {
        int normalized = PagingUtils.normalizePage(page);
        List<AuthorDto> content = authorService.findAll(PagingUtils.PAGE_SIZE, PagingUtils.offset(normalized));
        return PageResponse.of(content, normalized, PagingUtils.PAGE_SIZE,
                PagingUtils.totalPages(authorService.getCountPages()));
    }

    @GetMapping("/all")
    public List<AuthorDto> findAllUnpaged() {
        return authorService.findAll(Integer.MAX_VALUE, 0);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('librarian','admin')")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDto create(@Valid @RequestBody AuthorDto author) {
        if (authorService.findAuthorByFullName(author.getFullName()).isPresent()) {
            throw new IllegalStateException("Author already exists");
        }
        author.setId(null);
        Long id = authorService.save(author);
        log.debug("Author {} created", author.getFullName());
        return authorService.findById(id).orElseThrow(() -> new NoSuchElementException("Author not found: " + id));
    }
}
