package by.tms.service;

import by.tms.config.DatabaseConfigTest;
import by.tms.dao.AccountDao;
import by.tms.dto.user.AccountDto;
import by.tms.entity.user.Librarian;
import by.tms.entity.user.User;
import by.tms.entity.user.UserData;
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
class AccountServiceImplTest {

    @Autowired
    private AccountService accountService;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private AccountDao accountDao;

    @BeforeEach
    public void initDb() {
        TestDataImporter.importTestData(sessionFactory);
    }

    @AfterTestMethod
    public void flush() {
        sessionFactory.close();
    }

    @Test
    void findAllUsers() {
        List<AccountDto> allUsers = accountService.findAllUsers();
        assertEquals(2, allUsers.size());
    }

    @Test
    void findAllDebtors() {
        List<AccountDto> allDebtors = accountService.findAllDebtors();
        assertEquals(0, allDebtors.size());
    }

    @Test
    void findAllAdmins() {
        List<AccountDto> allAdmins = accountService.findAllAdmins();
        assertEquals(2, allAdmins.size());
    }

    @Test
    void findAllLibrarians() {
        List<AccountDto> allLibrarians = accountService.findAllLibrarians();
        assertEquals(2, allLibrarians.size());
        assertEquals("Kirill", allLibrarians.get(0).getUserData().getName());
    }

    @Test
    void findUserByUsername() {
        Optional<AccountDto> user = accountService.findUserByUsername("user");
        assertTrue(user.isPresent());
    }

    @Test
    void findUserById() {
        Optional<AccountDto> userById = accountService.findUserById(2L);
        userById.ifPresent(user -> assertEquals("newUser", user.getUsername()));
    }

    @Test
    void blockUser() {
        Optional<User> optionalUser = accountDao.findUserByUsername("user");
        optionalUser.ifPresent(user -> accountService.blockUser(user));
        Optional<AccountDto> userById = accountService.findUserByUsername("user");
        userById.ifPresent(user -> assertTrue(user.isBanned()));
    }

    @Test
    void saveAccount() {
        accountService.saveAccount(User.builder().username("cheburek").password("pass").userData(UserData.builder().build()).build());
        Optional<AccountDto> results = accountService.findUserByUsername("cheburek");
        assertTrue(results.isPresent());
    }

    @Test
    void updateAccount() {
        Optional<Librarian> optionalLibrarian = accountDao.findLibrarianById(6L);
        optionalLibrarian.ifPresent(librarian -> {
            librarian.setUsername("librarian");
            accountService.updateAccount(librarian);
        });
        Optional<AccountDto> librarianById = accountService.findLibrarianById(6L);
        librarianById.ifPresent(lib -> assertEquals("librarian", lib.getUsername()));
    }

    @Test
    void deleteAccount() {
        Optional<Librarian> optionalLibrarian = accountDao.findLibrarianById(5L);
        optionalLibrarian.ifPresent(librarian -> accountService.deleteAccount(librarian));
        Optional<AccountDto> librarianById = accountService.findLibrarianById(5L);
        assertFalse(librarianById.isPresent());
    }

    @Test
    void isAccountExist() {
        assertTrue(accountService.isAccountExist(3L));
    }
}