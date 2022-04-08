package by.tms.model.service;

import by.tms.model.dto.BookDto;
import by.tms.model.entity.Book;

import java.util.List;

public interface BookService extends GenericService<BookDto,Long, Book> {

    List<BookDto> findBookByName(String bookName);

    List<BookDto> findByAuthor(Long authorId, int limit, int offset);

    List<Long> getCountPages(Long authorId);
}
