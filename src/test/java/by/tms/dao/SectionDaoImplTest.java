package by.tms.dao;

import by.tms.config.DatabaseConfigTest;
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
class SectionDaoImplTest {

    @Autowired
    private SectionDao sectionDao;
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
        List<Section> results = sectionDao.findAll();
        assertEquals(3, results.size());
    }

    @Test
    void findById() {
        Optional<Section> byId = sectionDao.findById(1L);
        byId.ifPresent(section -> assertEquals("Дошкольная литература", section.getName()));
    }

    @Test
    void add() {
        sectionDao.save(Section.builder().name("Plants").build());
        Optional<Section> plants = sectionDao.findByName("Plants");
        assertTrue(plants.isPresent());
    }

    @Test
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
    void delete() {
        Optional<Section> byId = sectionDao.findById(3L);
        byId.ifPresent(section -> sectionDao.delete(section));
        Optional<Section> deleted = sectionDao.findById(3L);
        assertFalse(deleted.isPresent());
    }

    @Test
    void isSectionExist() {
        assertTrue(sectionDao.isExist(2L));
    }
}
