package by.tms.model.service;

import by.tms.model.dto.GenreDto;
import by.tms.model.entity.Genre;

import java.util.Optional;

public interface GenreService extends GenericService<GenreDto,Long, Genre> {

    Optional<GenreDto> findGenreByName(String genreName);
}
