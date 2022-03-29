package by.tms.model.service.impl;

import by.tms.model.dao.BookDao;
import by.tms.model.dto.BookDto;
import by.tms.model.entity.Book;
import by.tms.model.mapper.impl.BookMapper;
import by.tms.model.service.BookService;
import by.tms.model.util.LoggerUtil;
import by.tms.model.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;
    private final BookMapper bookMapper = BookMapper.getInstance();

    @Override
    public Optional<BookDto> findBookById(Long id) {
        Optional<Book> optionalBook = bookDao.findById(id);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_SERVICE_BY, optionalBook, id);
        return Optional.ofNullable(bookMapper.mapToDto(optionalBook.orElse(null)));
    }

    @Override
    public List<BookDto> findBookByName(String bookName) {
        List<Book> books = bookDao.findByName(bookName);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_SERVICE_BY, books, bookName);
        return bookMapper.mapToListDto(books);
    }

    @Override
    public List<BookDto> findByAuthor(Long authorId, int limit, int offset) {
        List<Book> byAuthor = bookDao.findByAuthor(authorId, limit, offset);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_SERVICE_BY, byAuthor, authorId);
        return bookMapper.mapToListDto(byAuthor);
    }

    @Override
    public List<BookDto> findAllBook(int limit, int offset) {
        List<Book> books = bookDao.findAll(limit, offset);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_SERVICE, books);
        return bookMapper.mapToListDto(books);
    }

    @Override
    @Transactional
    public Long addNewBook(BookDto bookDto) {
        Long saveBook = bookDao.save(bookMapper.mapToEntity(bookDto));
        log.debug(LoggerUtil.ENTITY_WAS_SAVED_IN_SERVICE, saveBook);
        return saveBook;
    }

    @Override
    @Transactional
    public void updateBook(BookDto bookDto) {
        bookDao.update(bookMapper.mapToEntity(bookDto));
        log.debug(LoggerUtil.ENTITY_WAS_UPDATED_IN_SERVICE, bookDto);
    }

    @Override
    @Transactional
    public void deleteBook(BookDto bookDto) {
        bookDao.delete(bookMapper.mapToEntity(bookDto));
        log.debug(LoggerUtil.ENTITY_WAS_DELETED_IN_SERVICE, bookDto);
    }

    @Override
    public boolean isBookExist(Long id) {
        boolean exist = bookDao.isExist(id);
        log.debug(LoggerUtil.ENTITY_IS_EXIST_IN_SERVICE_BY, exist, id);
        return exist;
    }

    @Override
    public List<Long> getCountPages() {
        Long countRow = bookDao.getCountRow();
        log.debug(LoggerUtil.COUNT_ROW_WAS_FOUND_IN_SERVICE, countRow);
        return ServiceUtil.collectPages(countRow);
    }

    @Override
    public List<Long> getCountPages(Long authorId) {
        Long countRow = bookDao.getCountRow(authorId);
        log.debug(LoggerUtil.COUNT_ROW_WAS_FOUND_IN_SERVICE, countRow);
        return ServiceUtil.collectPages(countRow);
    }
}
