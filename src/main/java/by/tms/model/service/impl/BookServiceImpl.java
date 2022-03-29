package by.tms.model.service.impl;

import by.tms.model.dao.BookDao;
import by.tms.model.dto.BookDto;
import by.tms.model.entity.Book;
import by.tms.model.service.BookService;
import by.tms.model.util.LoggerUtil;
import by.tms.model.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
@Slf4j
public class BookServiceImpl extends GenericServiceImpl<BookDto,Long,Book> implements BookService {

    private final BookDao bookDao;

    @Override
    public List<BookDto> findBookByName(String bookName) {
        List<Book> books = bookDao.findByName(bookName);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_SERVICE_BY, books, bookName);
        return mapper.mapToListDto(books);
    }

    @Override
    public List<BookDto> findByAuthor(Long authorId, int limit, int offset) {
        List<Book> byAuthor = bookDao.findByAuthor(authorId, limit, offset);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_SERVICE_BY, byAuthor, authorId);
        return mapper.mapToListDto(byAuthor);
    }

    @Override
    public List<Long> getCountPages(Long authorId) {
        Long countRow = bookDao.getCountRow(authorId);
        log.debug(LoggerUtil.COUNT_ROW_WAS_FOUND_IN_SERVICE, countRow);
        return ServiceUtil.collectPages(countRow);
    }
}
