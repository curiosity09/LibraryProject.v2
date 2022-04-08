package by.tms.model.dao;

import by.tms.model.config.DatabaseConfigTest;
import by.tms.model.entity.Genre;
import by.tms.util.TestDataImporter;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static by.tms.util.TestDataImporter.LIMIT_10;
import static by.tms.util.TestDataImporter.OFFSET_0;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DatabaseConfigTest.class)
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

    @AfterEach
    public void flush() {
        sessionFactory.close();
    }

    @Test
    void findAll() {
        List<Genre> results = genreDao.findAll(LIMIT_10,OFFSET_0);
        assertEquals(3, results.size());
    }

    @Test
    void findById() {
        Optional<Genre> byId = genreDao.findById(1L);
        byId.ifPresent(section -> assertEquals("Детектив", section.getName()));
    }

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