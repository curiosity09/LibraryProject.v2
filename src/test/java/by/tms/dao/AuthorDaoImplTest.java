package by.tms.dao;

import by.tms.config.DatabaseConfigTest;
import by.tms.entity.Author;
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
class AuthorDaoImplTest {

    @Autowired
    private AuthorDao authorDao;
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
        List<Author> results = authorDao.findAll();
        assertEquals(3, results.size());
    }

    @Test
    void findById() {
        Optional<Author> byId = authorDao.findById(1L);
        byId.ifPresent(author -> assertEquals("Борис Акунин", author.getFullName()));
    }

    @Test
    void add() {
        authorDao.save(Author.builder().fullName("Анджей Сапковский").build());
        Optional<Author> genre = authorDao.findByFullName("Анджей Сапковский");
        assertTrue(genre.isPresent());
    }

    @Test
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