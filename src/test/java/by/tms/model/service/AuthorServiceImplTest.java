package by.tms.model.service;

import by.tms.model.config.DatabaseConfigTest;
import by.tms.model.dto.AuthorDto;
import by.tms.model.util.TestDataImporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static by.tms.model.util.TestDataImporter.LIMIT_10;
import static by.tms.model.util.TestDataImporter.OFFSET_0;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DatabaseConfigTest.class)
@Transactional
class AuthorServiceImplTest {

    @Autowired
    private AuthorService authorService;
    @Autowired
    private TestDataImporter testDataImporter;

    @BeforeEach
    public void initDb() {
        testDataImporter.cleanTestData();
        testDataImporter.importTestData();
    }

    @Test
    void findAllAuthor() {
        List<AuthorDto> allAuthor = authorService.findAll(LIMIT_10,OFFSET_0);
        assertEquals(3, allAuthor.size());
    }

    @Test
    void addNewAuthor() {
        authorService.save(AuthorDto.builder().fullName("Анджей Сапковский").build());
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
        Optional<AuthorDto> optionalAuthor = authorService.findById(2L);
        optionalAuthor.ifPresent(author -> {
            author.setFullName("Виктор Ципеленен");
            authorService.update(author);
        });
        Optional<AuthorDto> authorById = authorService.findById(2L);
        authorById.ifPresent(authorDto -> assertEquals("Виктор Ципеленен", authorDto.getFullName()));
    }

    @Test
    void deleteAuthor() {
        Optional<AuthorDto> optionalAuthor = authorService.findById(1L);
        optionalAuthor.ifPresent(author -> authorService.delete(author));
        Optional<AuthorDto> authorById = authorService.findById(1L);
        assertFalse(authorById.isPresent());
    }

    @Test
    void findAuthorById() {
        Optional<AuthorDto> authorById = authorService.findById(3L);
        authorById.ifPresent(authorDto -> assertEquals("Чарльз Буковски", authorDto.getFullName()));
    }
}
