package by.tms.service;

import by.tms.dto.BookDto;
import by.tms.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Optional<BookDto> findBookById(Long id);

    List<BookDto> findBookByName(String bookName);

    List<BookDto> findAllBook();

    Long addNewBook(Book book);

    void updateBook(Book book);

    void deleteBook(Book book);

    boolean isBookExist(Long id);
}
