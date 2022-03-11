package by.tms.service;

import by.tms.config.DatabaseConfigTest;
import by.tms.dto.AuthorDto;
import by.tms.entity.Author;
import by.tms.util.TestDataImporter;
import org.hibernate.Session;
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
class AuthorServiceImplTest {

    @Autowired
    private AuthorService authorService;
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
    void findAllAuthor() {
        List<AuthorDto> allAuthor = authorService.findAllAuthor();
        assertEquals(3, allAuthor.size());
    }

    @Test
    void addNewAuthor() {
        authorService.addNewAuthor(Author.builder().fullName("Анджей Сапковский").build());
        Optional<AuthorDto> authorDto = authorService.findAuthorByFullName("Анджей Сапковский");
        assertTrue(authorDto.isPresent());
    }

    @Test
    void findAuthorByFullName() {
        Optional<AuthorDto> byFullName = authorService.findAuthorByFullName("Борис Акунин");
        assertTrue(byFullName.isPresent());
    }

    @Test
    void updateAuthor() {
        Session session = sessionFactory.getCurrentSession();
        Author author = session.find(Author.class, 2L);
        author.setFullName("Виктор Ципеленен");
        authorService.updateAuthor(author);
        Optional<AuthorDto> authorById = authorService.findAuthorById(2L);
        authorById.ifPresent(authorDto -> assertEquals("Виктор Ципеленен", authorDto.getFullName()));
    }

    @Test
    void deleteAuthor() {
        Session session = sessionFactory.getCurrentSession();
        Author author = session.find(Author.class, 1L);
        authorService.deleteAuthor(author);
        Optional<AuthorDto> authorById = authorService.findAuthorById(1L);
        assertFalse(authorById.isPresent());
    }

    @Test
    void isAuthorExist() {
       assertTrue(authorService.isAuthorExist(1L));
    }

    @Test
    void findAuthorById() {
        Optional<AuthorDto> authorById = authorService.findAuthorById(3L);
        authorById.ifPresent(authorDto -> assertEquals("Чарльз Буковски", authorDto.getFullName()));
    }
}