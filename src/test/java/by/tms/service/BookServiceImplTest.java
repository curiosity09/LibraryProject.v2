package by.tms.service;

import by.tms.config.DatabaseConfigTest;
import by.tms.dao.BookDao;
import by.tms.dto.BookDto;
import by.tms.entity.Book;
import by.tms.util.TestDataImporter;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.annotation.AfterTestMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DatabaseConfigTest.class)
@Transactional
class BookServiceImplTest {

    @Autowired
    private BookService bookService;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private BookDao bookDao;

    @BeforeEach
    public void initDb() {
        TestDataImporter.importTestData(sessionFactory);
    }

    @AfterTestMethod
    public void flush() {
        sessionFactory.close();
    }

    @Test
    void findBookById() {
        Optional<BookDto> bookById = bookService.findBookById(1L);
        bookById.ifPresent(bookDto -> assertEquals("Азазело", bookDto.getName()));
    }

    @Test
    void findBookByName() {
        List<BookDto> bookByName = bookService.findBookByName("Азазело");
        assertFalse(bookByName.isEmpty());
    }

    @Test
    void findAllBook() {
        List<BookDto> allBook = bookService.findAllBook();
        assertEquals(3, allBook.size());
    }

    @Test
    void addNewBook() {
        Book book = Book.builder().name("Владычица озера").quantity(1).publicationYear(1999).build();
        Long id = bookService.addNewBook(book);
        Optional<BookDto> byId = bookService.findBookById(id);
        assertTrue(byId.isPresent());
    }

    @Test
    void updateBook() {
        Optional<Book> byId = bookDao.findById(2L);
        byId.ifPresent(book -> {
            book.setName("Turkish Gambit");
            bookService.addNewBook(book);
        });
        Optional<BookDto> bookById = bookService.findBookById(2L);
        bookById.ifPresent(bookDto -> assertEquals("Turkish Gambit", bookDto.getName()));
    }

    @Test
    void deleteBook() {
        Optional<Book> byId = bookDao.findById(3L);
        byId.ifPresent(book -> bookService.deleteBook(book));
        Optional<BookDto> bookById = bookService.findBookById(3L);
        assertFalse(bookById.isPresent());
    }

    @Test
    void isBookExist() {
        assertTrue(bookService.isBookExist(2L));
    }
}