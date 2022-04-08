package by.tms.model.mapper.impl;

import by.tms.model.dto.GenreDto;
import by.tms.model.entity.Genre;
import by.tms.model.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class GenreMapper implements Mapper<Genre, GenreDto> {

    @Override
    public GenreDto mapToDto(Genre genre) {
        if (Objects.nonNull(genre)) {
            return GenreDto.builder()
                    .id(genre.getId())
                    .name(genre.getName())
                    .build();
        }
        return null;
    }

    @Override
    public List<GenreDto> mapToListDto(List<Genre> genres) {
        if (Objects.nonNull(genres)) {
            List<GenreDto> genreDtoList = new ArrayList<>();
            for (Genre genre : genres) {
                genreDtoList.add(mapToDto(genre));
            }
            return genreDtoList;
        }
        return Collections.emptyList();
    }

    @Override
    public Genre mapToEntity(GenreDto genreDto) {
        if (Objects.nonNull(genreDto)) {
            Genre genre = Genre.builder()
                    .name(genreDto.getName())
                    .build();
            genre.setId(genreDto.getId());
            return genre;
        }
        return null;
    }
}
