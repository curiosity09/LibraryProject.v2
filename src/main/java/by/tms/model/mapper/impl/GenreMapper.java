package by.tms.mapper.impl;

import by.tms.dto.GenreDto;
import by.tms.entity.Genre;
import by.tms.mapper.Mapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenreMapper implements Mapper<Genre, GenreDto> {

    private static final GenreMapper INSTANCE = new GenreMapper();

    public static GenreMapper getInstance() {
        return INSTANCE;
    }

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
