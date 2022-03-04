package by.tms.dao;

import by.tms.dao.impl.GenreDaoImpl;
import by.tms.entity.Genre;
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

public class GenreDaoImplTest {

    private final GenreDao genreDao = GenreDaoImpl.getInstance();
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
            List<Genre> results = genreDao.findAll(session);
            Assert.assertEquals(2, results.size());
            session.getTransaction().commit();
        }
    }

    @Test
    public void findById() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Genre> byId = genreDao.findById(session, 1L);
            byId.ifPresent(section -> Assert.assertEquals("Детектив", section.getName()));
            session.getTransaction().commit();
        }
    }

    @Test
    public void add() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            genreDao.add(session, Genre.builder().name("Комедия").build());
            Optional<Genre> genre = genreDao.findByName(session, "Комедия");
            Assert.assertTrue(genre.isPresent());
            session.getTransaction().commit();
        }
    }

    @Test
    public void update() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Genre> byId = genreDao.findById(session, 2L);
            byId.ifPresent(genre -> {
                genre.setName("Roman");
                genreDao.update(session, genre);
            });
            Optional<Genre> updatedGenre = genreDao.findById(session, 2L);
            updatedGenre.ifPresent(genre -> Assert.assertEquals("Roman", genre.getName()));
            session.getTransaction().commit();
        }
    }
}