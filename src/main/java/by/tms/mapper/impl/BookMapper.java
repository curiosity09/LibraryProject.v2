package by.tms.mapper.impl;

import by.tms.dto.AuthorDto;
import by.tms.dto.BookDto;
import by.tms.dto.GenreDto;
import by.tms.dto.SectionDto;
import by.tms.entity.Book;
import by.tms.mapper.Mapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookMapper implements Mapper<Book, BookDto> {

    private static final BookMapper INSTANCE = new BookMapper();
    private final AuthorMapper authorMapper = AuthorMapper.getInstance();
    private final GenreMapper genreMapper = GenreMapper.getInstance();
    private final SectionMapper sectionMapper = SectionMapper.getInstance();

    public static BookMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public BookDto mapToDto(Book book) {
        if (Objects.nonNull(book)) {
            return BookDto.builder()
                    .id(book.getId())
                    .name(book.getName())
                    .author(authorMapper.mapToDto(book.getAuthor()))
                    .genre(genreMapper.mapToDto(book.getGenre()))
                    .section(sectionMapper.mapToDto(book.getSection()))
                    .quantity(book.getQuantity())
                    .publicationYear(book.getPublicationYear())
                    .build();
        }
        return null;
    }

    @Override
    public List<BookDto> mapToListDto(List<Book> books) {
        if (Objects.nonNull(books)) {
            List<BookDto> bookDtoList = new ArrayList<>();
            for (Book book : books) {
                bookDtoList.add(mapToDto(book));
            }
            return bookDtoList;
        }
        return Collections.emptyList();
    }
}
