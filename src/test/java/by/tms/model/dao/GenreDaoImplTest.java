package by.tms.model.dao;

import by.tms.model.config.HibernateConfigTest;
import by.tms.model.entity.Genre;
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
class GenreDaoImplTest {

    @Autowired
    private GenreDao genreDao;
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
        List<Genre> results = genreDao.findAll();
        assertEquals(3, results.size());
    }

    @Test
    void findById() {
        Optional<Genre> byId = genreDao.findById(1L);
        byId.ifPresent(section -> assertEquals("Детектив", section.getName()));
    }

/*    @Test
    void isExist() {
        assertTrue(genreDao.isExist(3L));
    }*/

    @Test
    @Transactional
    void add() {
        genreDao.save(Genre.builder().name("Комедия").build());
        Optional<Genre> genre = genreDao.findByName("Комедия");
        assertTrue(genre.isPresent());
    }

    @Test
    @Transactional
    void update() {
        Optional<Genre> byId = genreDao.findById(2L);
        byId.ifPresent(genre -> {
            genre.setName("Roman");
            genreDao.update(genre);
        });
        Optional<Genre> updatedGenre = genreDao.findById(2L);
        updatedGenre.ifPresent(genre -> assertEquals("Roman", genre.getName()));
    }
}