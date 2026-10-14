package by.tms.model.dao;

import by.tms.model.entity.Author;
import by.tms.model.util.TestDataImporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static by.tms.model.util.TestDataImporter.LIMIT_10;
import static by.tms.model.util.TestDataImporter.OFFSET_0;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional(readOnly = true)
class AuthorDaoImplTest {

    @Autowired
    private AuthorDao authorDao;
    @Autowired
    private TestDataImporter testDataImporter;

    @BeforeEach
    public void initDb() {
        testDataImporter.cleanTestData();
        testDataImporter.importTestData();
    }

    @Test
    void findAll() {
        List<Author> results = authorDao.findAll(LIMIT_10, OFFSET_0);
        assertEquals(3, results.size());
    }

    @Test
    void findById() {
        Optional<Author> byId = authorDao.findById(1L);
        byId.ifPresent(author -> assertEquals("Борис Акунин", author.getFullName()));
    }

    @Test
    @Transactional
    void add() {
        authorDao.save(Author.builder().fullName("Анджей Сапковский").build());
        Optional<Author> genre = authorDao.findByFullName("Анджей Сапковский");
        assertTrue(genre.isPresent());
    }

    @Test
    @Transactional
    void update() {
        Optional<Author> byId = authorDao.findById(2L);
        byId.ifPresent(author -> {
            author.setFullName("Джордж Мартин");
            authorDao.update(author);
        });
        Optional<Author> updatedAuthor = authorDao.findById(2L);
        updatedAuthor.ifPresent(author -> assertEquals("Джордж Мартин", author.getFullName()));
    }
}
