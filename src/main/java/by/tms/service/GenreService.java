package by.tms.service;

import by.tms.dto.GenreDto;
import by.tms.entity.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {

    List<GenreDto> findAllGenre();

    Long addNewGenre(Genre genre);

    Optional<GenreDto> findGenreByName(String genreName);

    void updateGenre(Genre genre);

    void deleteGenre(Genre genre);

    boolean isGenreExist(Long id);

    Optional<GenreDto> findGenreById(Long id);
}
