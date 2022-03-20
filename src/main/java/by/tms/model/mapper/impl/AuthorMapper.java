package by.tms.model.mapper.impl;

import by.tms.model.dto.AuthorDto;
import by.tms.model.entity.Author;
import by.tms.model.mapper.Mapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorMapper implements Mapper<Author, AuthorDto> {

    private static final AuthorMapper INSTANCE = new AuthorMapper();

    public static AuthorMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public AuthorDto mapToDto(Author author) {
        if (Objects.nonNull(author)) {
            return AuthorDto.builder()
                    .id(author.getId())
                    .fullName(author.getFullName())
                    .build();
        }
        return null;
    }

    @Override
    public List<AuthorDto> mapToListDto(List<Author> authors) {
        if (Objects.nonNull(authors)) {
            List<AuthorDto> authorDtoList = new ArrayList<>();
            for (Author author : authors) {
                authorDtoList.add(mapToDto(author));
            }
            return authorDtoList;
        }
        return Collections.emptyList();
    }

    @Override
    public Author mapToEntity(AuthorDto authorDto) {
        if (Objects.nonNull(authorDto)) {
            Author author = Author.builder()
                    .fullName(authorDto.getFullName())
                    .build();
            author.setId(authorDto.getId());
            return author;
        }
        return null;
    }
}
