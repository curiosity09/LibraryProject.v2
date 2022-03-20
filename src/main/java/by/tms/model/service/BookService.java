package by.tms.model.service;

import by.tms.model.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Optional<BookDto> findBookById(Long id);

    List<BookDto> findBookByName(String bookName);

    List<BookDto> findAllBook();

    Long addNewBook(BookDto bookDto);

    void updateBook(BookDto bookDto);

    void deleteBook(BookDto bookDto);

    boolean isBookExist(Long id);
}
