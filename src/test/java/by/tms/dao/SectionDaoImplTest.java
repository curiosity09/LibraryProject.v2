package by.tms.dao;

import by.tms.dao.impl.SectionDaoImpl;
import by.tms.entity.Section;
import by.tms.util.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class SectionDaoImplTest {

    private final SectionDao sectionDao = SectionDaoImpl.getInstance();
    private SessionFactory sessionFactory;

    @Before
    public void initDb() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
        TestDataImporter.getInstance().importTestData(sessionFactory);
    }

    @After
    public void finish() {
        sessionFactory.close();
    }

    @Test
    public void findAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Section> results = sectionDao.findAll(session);
            Assert.assertEquals(3, results.size());
            session.getTransaction().commit();
        }
    }

    @Test
    public void findById() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Section> byId = sectionDao.findById(session, 1L);
            byId.ifPresent(section -> Assert.assertEquals("Дошкольная литература", section.getName()));
            session.getTransaction().commit();
        }
    }

    @Test
    public void add() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            sectionDao.add(session, Section.builder().name("Plants").build());
            Optional<Section> plants = sectionDao.findByName(session, "Plants");
            Assert.assertTrue(plants.isPresent());
            session.getTransaction().commit();
        }
    }

    @Test
    public void update() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Section> byId = sectionDao.findById(session, 2L);
            byId.ifPresent(section -> {
                section.setName("Planets");
                sectionDao.update(session, section);
            });
            Optional<Section> updatedSection = sectionDao.findById(session, 2L);
            updatedSection.ifPresent(section -> Assert.assertEquals("Planets", section.getName()));
            session.getTransaction().commit();
        }
    }
}