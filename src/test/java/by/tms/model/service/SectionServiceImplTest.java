package by.tms.model.service;

import by.tms.model.config.DatabaseConfigTest;
import by.tms.model.dto.SectionDto;
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
@Transactional
class SectionServiceImplTest {

    @Autowired
    private SectionService sectionService;
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
    void findAllSection() {
        List<SectionDto> results = sectionService.findAllSection(LIMIT_10,OFFSET_0);
        assertEquals(3, results.size());
    }

    @Test
    void addNewSection() {
        sectionService.addNewSection(SectionDto.builder().name("Plants").build());
        Optional<SectionDto> plants = sectionService.findSectionByName("Plants");
        assertTrue(plants.isPresent());
    }

    @Test
    void findSectionById() {
        Optional<SectionDto> sectionById = sectionService.findSectionById(2L);
        sectionById.ifPresent(sectionDto -> assertEquals("Космос",sectionDto.getName()));
    }

    @Test
    void findSectionByName() {
        Optional<SectionDto> sectionByName = sectionService.findSectionByName("Косм1ос");
        assertFalse(sectionByName.isPresent());
    }

    @Test
    void updateSection() {
        Optional<SectionDto> byName = sectionService.findSectionByName("Космос");
        byName.ifPresent(section -> {
            section.setName("Planets");
            sectionService.updateSection(section);
        });
        Optional<SectionDto> updatedSection = sectionService.findSectionById(2L);
        updatedSection.ifPresent(planets -> assertEquals("Planets", planets.getName()));
    }

    @Test
    void deleteSection() {
        Optional<SectionDto> byId = sectionService.findSectionById(3L);
        byId.ifPresent(section -> sectionService.deleteSection(section));
        Optional<SectionDto> sectionById = sectionService.findSectionById(3L);
        assertFalse(sectionById.isPresent());
    }
}