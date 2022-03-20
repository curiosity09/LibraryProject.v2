package by.tms.model.service;

import by.tms.model.config.HibernateConfigTest;
import by.tms.model.dto.AuthorDto;
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
        authorService.addNewAuthor(AuthorDto.builder().fullName("Анджей Сапковский").build());
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
        Optional<AuthorDto> optionalAuthor = authorService.findAuthorById(2L);
        optionalAuthor.ifPresent(author -> {
            author.setFullName("Виктор Ципеленен");
            authorService.updateAuthor(author);
        });
        Optional<AuthorDto> authorById = authorService.findAuthorById(2L);
        authorById.ifPresent(authorDto -> assertEquals("Виктор Ципеленен", authorDto.getFullName()));
    }

    @Test
    void deleteAuthor() {
        Optional<AuthorDto> optionalAuthor = authorService.findAuthorById(1L);
        optionalAuthor.ifPresent(author -> authorService.deleteAuthor(author));
        Optional<AuthorDto> authorById = authorService.findAuthorById(1L);
        assertFalse(authorById.isPresent());
    }

/*    @Test
    void isAuthorExist() {
        assertTrue(authorService.isAuthorExist(1L));
    }*/

    @Test
    void findAuthorById() {
        Optional<AuthorDto> authorById = authorService.findAuthorById(3L);
        authorById.ifPresent(authorDto -> assertEquals("Чарльз Буковски", authorDto.getFullName()));
    }
}
