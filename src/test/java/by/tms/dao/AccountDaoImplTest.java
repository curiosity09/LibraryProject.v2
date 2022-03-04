package by.tms.dao;

import by.tms.dao.impl.AccountDaoImpl;
import by.tms.entity.user.Account;
import by.tms.entity.user.Admin;
import by.tms.entity.user.Librarian;
import by.tms.entity.user.User;
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

public class AccountDaoImplTest {

    private final AccountDao accountDao = AccountDaoImpl.getInstance();
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
            List<Account> results = accountDao.findAll(session);
            Assert.assertEquals(3, results.size());
            session.getTransaction().commit();
        }
    }

    @Test
    public void findAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<User> results = accountDao.findAllUsers(session);
            Assert.assertEquals("user", results.get(0).getUsername());
            session.getTransaction().commit();
        }
    }

    @Test
    public void findAllAdmins() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Admin> results = accountDao.findAllAdmins(session);
            Assert.assertEquals("admin", results.get(0).getUsername());
            session.getTransaction().commit();
        }
    }

    @Test
    public void findAllLibrarians() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Librarian> results = accountDao.findAllLibrarians(session);
            Assert.assertEquals("lib", results.get(0).getUsername());
            session.getTransaction().commit();
        }
    }

    @Test
    public void findByUsername() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Account> results = accountDao.findByUsername(session, "user");
            results.ifPresent(user -> Assert.assertEquals("user", user.getUsername()));
            session.getTransaction().commit();
        }
    }

    @Test
    public void add() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            accountDao.add(session, User.builder().username("cheburek").password("pass").build());
            Optional<Account> results = accountDao.findByUsername(session, "cheburek");
            Assert.assertTrue(results.isPresent());
            session.getTransaction().commit();
        }
    }

    @Test
    public void update() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Account> byId = accountDao.findById(session, 3L);
            byId.ifPresent(account -> {
                account.setUsername("librarian");
                accountDao.update(session, account);
            });
            Optional<Account> updatedAccount = accountDao.findById(session, 3L);
            updatedAccount.ifPresent(account -> Assert.assertEquals("librarian", account.getUsername()));
            session.getTransaction().commit();
        }
    }
}