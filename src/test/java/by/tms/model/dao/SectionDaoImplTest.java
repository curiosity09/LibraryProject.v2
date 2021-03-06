package by.tms.model.dao;

import by.tms.model.config.DatabaseConfigTest;
import by.tms.model.entity.Section;
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
@Transactional(readOnly = true)
class SectionDaoImplTest {

    @Autowired
    private SectionDao sectionDao;
    @Autowired
    private TestDataImporter testDataImporter;

    @BeforeEach
    public void initDb() {
        testDataImporter.cleanTestData();
        testDataImporter.importTestData();
    }

    @Test
    void findAll() {
        List<Section> results = sectionDao.findAll(LIMIT_10,OFFSET_0);
        assertEquals(3, results.size());
    }

    @Test
    void findById() {
        Optional<Section> byId = sectionDao.findById(1L);
        byId.ifPresent(section -> assertEquals("Дошкольная литература", section.getName()));
    }

    @Test
    @Transactional
    void add() {
        sectionDao.save(Section.builder().name("Plants").build());
        Optional<Section> plants = sectionDao.findByName("Plants");
        assertTrue(plants.isPresent());
    }

    @Test
    @Transactional
    void update() {
        Optional<Section> byId = sectionDao.findById(2L);
        byId.ifPresent(section -> {
            section.setName("Planets");
            sectionDao.update(section);
        });
        Optional<Section> updatedSection = sectionDao.findById(2L);
        updatedSection.ifPresent(section -> assertEquals("Planets", section.getName()));
    }

    @Test
    @Transactional
    void delete() {
        Optional<Section> byId = sectionDao.findById(3L);
        byId.ifPresent(section -> sectionDao.delete(section));
        Optional<Section> deleted = sectionDao.findById(3L);
        assertFalse(deleted.isPresent());
    }
}
