package by.tms.dao;

import by.tms.dao.impl.AuthorDaoImpl;
import by.tms.entity.Author;
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

public class AuthorDaoImplTest {

    private final AuthorDao authorDao = AuthorDaoImpl.getInstance();
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
            List<Author> results = authorDao.findAll(session);
            Assert.assertEquals(2, results.size());
            session.getTransaction().commit();
        }
    }

    @Test
    public void findById() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Author> byId = authorDao.findById(session, 1L);
            byId.ifPresent(author -> Assert.assertEquals("Борис Акунин", author.getFullName()));
            session.getTransaction().commit();
        }
    }

    @Test
    public void add() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            authorDao.add(session, Author.builder().fullName("Анджей Сапковский").build());
            Optional<Author> genre = authorDao.findByFullName(session, "Анджей Сапковский");
            Assert.assertTrue(genre.isPresent());
            session.getTransaction().commit();
        }
    }

    @Test
    public void update() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Author> byId = authorDao.findById(session, 2L);
            byId.ifPresent(author -> {
                author.setFullName("Джордж Мартин");
                authorDao.update(session, author);
            });
            Optional<Author> updatedAuthor = authorDao.findById(session, 2L);
            updatedAuthor.ifPresent(author -> Assert.assertEquals("Джордж Мартин", author.getFullName()));
            session.getTransaction().commit();
        }
    }
}