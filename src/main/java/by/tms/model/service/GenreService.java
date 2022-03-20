package by.tms.service;

import by.tms.dto.GenreDto;

import java.util.List;
import java.util.Optional;

public interface GenreService {

    List<GenreDto> findAllGenre();

    Long addNewGenre(GenreDto genreDto);

    Optional<GenreDto> findGenreByName(String genreName);

    void updateGenre(GenreDto genreDto);

    void deleteGenre(GenreDto genreDto);

    boolean isGenreExist(Long id);

    Optional<GenreDto> findGenreById(Long id);
}
