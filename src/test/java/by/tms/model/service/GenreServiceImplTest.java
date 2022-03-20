package by.tms.service;

import by.tms.config.DatabaseConfigTest;
import by.tms.dto.GenreDto;
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
class GenreServiceImplTest {

    @Autowired
    private GenreService genreService;
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
    void findAllGenre() {
        List<GenreDto> allGenre = genreService.findAllGenre();
        assertEquals(3, allGenre.size());
    }

    @Test
    void addNewGenre() {
        genreService.addNewGenre(GenreDto.builder().name("Комедия").build());
        Optional<GenreDto> genre = genreService.findGenreByName("Комедия");
        assertTrue(genre.isPresent());
    }

    @Test
    void findGenreByName() {
        Optional<GenreDto> genre = genreService.findGenreByName("Роман");
        assertTrue(genre.isPresent());
    }

    @Test
    void updateGenre() {
        Optional<GenreDto> byId = genreService.findGenreById(1L);
        byId.ifPresent(genre -> {
            genre.setName("Detective");
            genreService.updateGenre(genre);
        });
        Optional<GenreDto> genreById = genreService.findGenreById(1L);
        genreById.ifPresent(genreDto -> assertEquals("Detective", genreDto.getName()));
    }

    @Test
    void deleteGenre() {
        Optional<GenreDto> optionalGenre = genreService.findGenreById(2L);
        optionalGenre.ifPresent(genreDto -> genreService.deleteGenre(genreDto));
        Optional<GenreDto> genreById = genreService.findGenreById(2L);
        assertFalse(genreById.isPresent());
    }

/*    @Test
    void isGenreExist() {
        assertTrue(genreService.isGenreExist(3L));
    }*/

    @Test
    void findGenreById() {
        Optional<GenreDto> genreById = genreService.findGenreById(3L);
        genreById.ifPresent(genreDto -> assertEquals("Автобиография", genreDto.getName()));
    }
}