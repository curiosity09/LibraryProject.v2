package by.tms.model.service;

import by.tms.model.config.DatabaseConfigTest;
import by.tms.model.dto.GenreDto;
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
class GenreServiceImplTest {

    @Autowired
    private GenreService genreService;
    @Autowired
    private TestDataImporter testDataImporter;

    @BeforeEach
    public void initDb() {
        testDataImporter.cleanTestData();
        testDataImporter.importTestData();
    }

    @Test
    void findAllGenre() {
        List<GenreDto> allGenre = genreService.findAll(LIMIT_10,OFFSET_0);
        assertEquals(3, allGenre.size());
    }

    @Test
    void addNewGenre() {
        genreService.save(GenreDto.builder().name("Комедия").build());
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
        Optional<GenreDto> byId = genreService.findById(1L);
        byId.ifPresent(genre -> {
            genre.setName("Detective");
            genreService.update(genre);
        });
        Optional<GenreDto> genreById = genreService.findById(1L);
        genreById.ifPresent(genreDto -> assertEquals("Detective", genreDto.getName()));
    }

    @Test
    void deleteGenre() {
        Optional<GenreDto> optionalGenre = genreService.findById(2L);
        optionalGenre.ifPresent(genreDto -> genreService.delete(genreDto));
        Optional<GenreDto> genreById = genreService.findById(2L);
        assertFalse(genreById.isPresent());
    }

    @Test
    void findGenreById() {
        Optional<GenreDto> genreById = genreService.findById(3L);
        genreById.ifPresent(genreDto -> assertEquals("Автобиография", genreDto.getName()));
    }
}