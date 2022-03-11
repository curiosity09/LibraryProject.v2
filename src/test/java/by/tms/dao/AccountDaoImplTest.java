package by.tms.dao;

import by.tms.config.DatabaseConfigTest;
import by.tms.dto.user.AccountDto;
import by.tms.entity.user.Account;
import by.tms.entity.user.Admin;
import by.tms.entity.user.Librarian;
import by.tms.entity.user.User;
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
@Transactional()
class AccountDaoImplTest {

    @Autowired
    private AccountDao accountDao;
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
        List<Account> results = accountDao.findAll();
        assertEquals(6, results.size());
    }

    @Test
    void findAllUsers() {
        List<User> results = accountDao.findAllUsers();
        assertEquals("user", results.get(0).getUsername());
    }

    @Test
    void findAllAdmins() {
        List<Admin> results = accountDao.findAllAdmins();
        assertEquals("admin", results.get(0).getUsername());
    }

    @Test
    void findAllLibrarians() {
        List<Librarian> results = accountDao.findAllLibrarians();
        assertEquals("lib", results.get(0).getUsername());
    }

    @Test
    void findByUsername() {
        Optional<User> results = accountDao.findUserByUsername("user");
        results.ifPresent(user -> assertEquals("user", user.getUsername()));
    }

    @Test
    void add() {
        accountDao.save(User.builder().username("cheburek").password("pass").build());
        Optional<User> results = accountDao.findUserByUsername("cheburek");
        assertTrue(results.isPresent());
    }

    @Test
    void update() {
        Optional<Account> byId = accountDao.findById(3L);
        byId.ifPresent(account -> {
            account.setUsername("librarian");
            accountDao.update(account);
        });
        Optional<Account> updatedAccount = accountDao.findById(3L);
        updatedAccount.ifPresent(account -> assertEquals("librarian", account.getUsername()));
    }

    @Test
    void findUserById() {
        Optional<User> userById = accountDao.findUserById(1L);
        assertTrue(userById.isPresent());
    }

    @Test
    void findAllDebtors() {
        List<User> allDebtors = accountDao.findAllDebtors();
        assertEquals(0, allDebtors.size());
    }
}