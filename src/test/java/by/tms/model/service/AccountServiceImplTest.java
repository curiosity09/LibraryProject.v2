package by.tms.service;

import by.tms.config.DatabaseConfigTest;
import by.tms.dto.user.AccountDto;
import by.tms.dto.user.UserDataDto;
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
class AccountServiceImplTest {

    @Autowired
    private AccountService accountService;
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
        Optional<AccountDto> optionalUser = accountService.findUserByUsername("user");
        optionalUser.ifPresent(user -> accountService.blockUser(user));
        Optional<AccountDto> userById = accountService.findUserByUsername("user");
        userById.ifPresent(user -> assertTrue(user.isBanned()));
    }

    @Test
    void saveUser() {
        accountService.saveUser(AccountDto.builder().username("cheburek").password("pass").userData(UserDataDto.builder().build()).build());
        Optional<AccountDto> results = accountService.findUserByUsername("cheburek");
        assertTrue(results.isPresent());
    }

    @Test
    void updateAccount() {
        Optional<AccountDto> optionalLibrarian = accountService.findLibrarianById(6L);
        optionalLibrarian.ifPresent(librarian -> {
            librarian.setUsername("librarian");
            accountService.updateLibrarian(librarian);
        });
        Optional<AccountDto> librarianById = accountService.findLibrarianById(6L);
        librarianById.ifPresent(lib -> assertEquals("librarian", lib.getUsername()));
    }

    @Test
    void deleteAccount() {
        Optional<AccountDto> optionalLibrarian = accountService.findLibrarianById(5L);
        optionalLibrarian.ifPresent(librarian -> accountService.deleteAccount(librarian));
        Optional<AccountDto> librarianById = accountService.findLibrarianById(5L);
        assertFalse(librarianById.isPresent());
    }

    @Test
    void findByUsername() {
        Optional<AccountDto> lib = accountService.findAccountByUsername("l1ib");
        assertFalse(lib.isPresent());
    }
}