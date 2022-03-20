package by.tms.model.dao;

import by.tms.model.config.HibernateConfigTest;
import by.tms.model.entity.Book;
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
@ContextConfiguration(classes = HibernateConfigTest.class)
@Transactional(readOnly = true)
class BookDaoImplTest {

    @Autowired
    private BookDao bookDao;
    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    public void initDb() {
        TestDataImporter.importTestData(sessionFactory);
    }

    @AfterTestMethod
    public void flush() {
        sessionFactory.close();
    }

    @Test
    void findAll() {
        List<Book> results = bookDao.findAll();
        assertEquals(3, results.size());
    }

    @Test
    void findById() {
        Optional<Book> byId = bookDao.findById(1L);
        byId.ifPresent(book -> {
            assertEquals("Азазело", book.getName());
            assertEquals("Борис Акунин", book.getAuthor().getFullName());
        });
    }

    @Test
    @Transactional
    void add() {
        Book book = Book.builder().name("Владычица озера").quantity(1).publicationYear(1999).build();
        Long id = bookDao.save(book);
        Optional<Book> byId = bookDao.findById(id);
        assertTrue(byId.isPresent());
    }

    @Test
    @Transactional
    void update() {
        Optional<Book> byId = bookDao.findById(1L);
        byId.ifPresent(book -> {
            book.setName("Последнее желание");
            bookDao.update(book);
        });
        Optional<Book> updatedBook = bookDao.findById(1L);
        updatedBook.ifPresent(book -> assertEquals("Последнее желание", book.getName()));
    }
}