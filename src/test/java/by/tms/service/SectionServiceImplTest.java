package by.tms.service;

import by.tms.config.DatabaseConfigTest;
import by.tms.dao.SectionDao;
import by.tms.dto.SectionDto;
import by.tms.entity.Section;
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
class SectionServiceImplTest {

    @Autowired
    private SectionService sectionService;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private SectionDao sectionDao;

    @BeforeEach
    public void initDb() {
        TestDataImporter.importTestData(sessionFactory);
    }

    @AfterTestMethod
    public void flush() {
        sessionFactory.close();
    }

    @Test
    void findAllSection() {
        List<SectionDto> results = sectionService.findAllSection();
        assertEquals(3, results.size());
    }

    @Test
    void addNewSection() {
        sectionService.addNewSection(Section.builder().name("Plants").build());
        Optional<SectionDto> plants = sectionService.findSectionByName("Plants");
        assertTrue(plants.isPresent());
    }

    @Test
    void isSectionExist() {
        assertTrue(sectionService.isSectionExist(2L));
    }

    @Test
    void findSectionById() {
        Optional<SectionDto> sectionById = sectionService.findSectionById(2L);
        sectionById.ifPresent(sectionDto -> assertEquals("Космос",sectionDto.getName()));
    }

    @Test
    void findSectionByName() {
        Optional<SectionDto> sectionByName = sectionService.findSectionByName("Космос");
        assertTrue(sectionByName.isPresent());
    }

    @Test
    void updateSection() {
        Optional<Section> byId = sectionDao.findById(2L);
        byId.ifPresent(section -> {
            section.setName("Planets");
            sectionService.updateSection(section);
        });
        Optional<SectionDto> updatedSection = sectionService.findSectionById(2L);
        updatedSection.ifPresent(planets -> assertEquals("Planets", planets.getName()));
    }

    @Test
    void deleteSection() {
        Optional<Section> byId = sectionDao.findById(3L);
        byId.ifPresent(section -> sectionService.deleteSection(section));
        Optional<SectionDto> sectionById = sectionService.findSectionById(3L);
        assertFalse(sectionById.isPresent());
    }
}