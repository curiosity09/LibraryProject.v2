package by.tms.model.dao;

import by.tms.model.config.HibernateConfigTest;
import by.tms.model.entity.Author;
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