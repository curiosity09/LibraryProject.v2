package by.tms.dao;


import by.tms.dao.impl.BookDaoImpl;
import by.tms.entity.Book;
import by.tms.util.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class BookDaoImplTest {

    private final BookDao bookDao = BookDaoImpl.getInstance();
    private SessionFactory sessionFactory;

    @Before
    public void initDb() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
        TestDataImporter.getInstance().importTestData(sessionFactory);
    }

    @After
    public void finish() {
        sessionFactory.close();
    }

    @Test
    public void findAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Book> results = bookDao.findAll(session);
            Assert.assertEquals(3, results.size());
            session.getTransaction().commit();
        }
    }

    @Test
    public void findById() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Book> byId = bookDao.findById(session, 1L);
            byId.ifPresent(book -> {
                Assert.assertEquals("Азазело", book.getName());
                Assert.assertEquals("Борис Акунин", book.getAuthor().getFullName());
            });
            session.getTransaction().commit();
        }
    }

    @Test
    public void add() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Book book = Book.builder().name("Владычица озера").quantity(1).publicationYear(1999).build();
            bookDao.add(session, book);
            Optional<Book> byId = bookDao.findById(session, 4L);
            Assert.assertTrue(byId.isPresent());
            session.getTransaction().commit();
        }
    }

    @Test
    public void update() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Book> byId = bookDao.findById(session, 1L);
            byId.ifPresent(book -> {
                book.setName("Последнее желание");
                bookDao.update(session, book);
            });
            Optional<Book> updatedBook = bookDao.findById(session, 1L);
            updatedBook.ifPresent(book -> Assert.assertEquals("Последнее желание", book.getName()));
            session.getTransaction().commit();
        }
    }
}