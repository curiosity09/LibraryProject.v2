package by.tms.web.rest;

import by.tms.model.dto.GenreDto;
import by.tms.model.service.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
@Slf4j
public class GenreRestController {

    private final GenreService genreService;

    @GetMapping
    public List<GenreDto> findAll() {
        return genreService.findAll(Integer.MAX_VALUE, 0);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('librarian','admin')")
    @ResponseStatus(HttpStatus.CREATED)
    public GenreDto create(@Valid @RequestBody GenreDto genre) {
        if (genreService.findGenreByName(genre.getName()).isPresent()) {
            throw new IllegalStateException("Genre already exists");
        }
        genre.setId(null);
        Long id = genreService.save(genre);
        log.debug("Genre {} created", genre.getName());
        return genreService.findById(id).orElseThrow(() -> new NoSuchElementException("Genre not found: " + id));
    }
}
