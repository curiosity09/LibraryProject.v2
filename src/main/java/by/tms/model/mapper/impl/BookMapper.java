package by.tms.model.mapper.impl;

import by.tms.model.dto.BookDto;
import by.tms.model.entity.Book;
import by.tms.model.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookMapper implements Mapper<Book, BookDto> {

    private final AuthorMapper authorMapper;
    private final GenreMapper genreMapper;
    private final SectionMapper sectionMapper;

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

    @Override
    public Book mapToEntity(BookDto bookDto) {
        if (Objects.nonNull(bookDto)) {
            Book book = Book.builder()
                    .name(bookDto.getName())
                    .author(authorMapper.mapToEntity(bookDto.getAuthor()))
                    .genre(genreMapper.mapToEntity(bookDto.getGenre()))
                    .section(sectionMapper.mapToEntity(bookDto.getSection()))
                    .quantity(bookDto.getQuantity())
                    .publicationYear(bookDto.getPublicationYear())
                    .build();
            book.setId(bookDto.getId());
            return book;
        }
        return null;
    }

    public List<Book> mapToListEntity(List<BookDto> books) {
        if (Objects.nonNull(books)) {
            List<Book> bookList = new ArrayList<>();
            for (BookDto book : books) {
                bookList.add(mapToEntity(book));
            }
            return bookList;
        }
        return Collections.emptyList();
    }
}
