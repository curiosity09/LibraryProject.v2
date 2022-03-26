package by.tms.model.service;

import by.tms.model.config.DatabaseConfigTest;
import by.tms.model.dto.user.AccountDto;
import by.tms.model.dto.user.UserDataDto;
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
class AccountServiceImplTest {

    @Autowired
    private AccountService accountService;
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
    void findAllUsers() {
        List<AccountDto> allUsers = accountService.findAllUsers(LIMIT_10,OFFSET_0);
        assertEquals(2, allUsers.size());
    }

    @Test
    void findAllDebtors() {
        List<AccountDto> allDebtors = accountService.findAllDebtors();
        assertEquals(0, allDebtors.size());
    }

    @Test
    void findAllAdmins() {
        List<AccountDto> allAdmins = accountService.findAllAdmins(LIMIT_10,OFFSET_0);
        assertEquals(2, allAdmins.size());
    }

    @Test
    void findAllLibrarians() {
        List<AccountDto> allLibrarians = accountService.findAllLibrarians(LIMIT_10,OFFSET_0);
        assertEquals(2, allLibrarians.size());
        assertEquals("Kirill", allLibrarians.get(0).getUserData().getName());
    }

    @Test
    void findUserByUsername() {
        Optional<AccountDto> user = accountService.findAccountByUsername("user");
        assertTrue(user.isPresent());
    }

    @Test
    void findUserById() {
        Optional<AccountDto> userById = accountService.findAccountById(2L);
        userById.ifPresent(user -> assertEquals("newUser", user.getUsername()));
    }

    @Test
    void blockUser() {
        Optional<AccountDto> optionalUser = accountService.findAccountByUsername("user");
        optionalUser.ifPresent(user -> accountService.blockUser(user));
        Optional<AccountDto> userById = accountService.findAccountByUsername("user");
        userById.ifPresent(user -> assertTrue(user.isBanned()));
    }

    @Test
    void saveUser() {
        accountService.saveUser(AccountDto.builder().username("cheburek").password("pass").userData(UserDataDto.builder().build()).build());
        Optional<AccountDto> results = accountService.findAccountByUsername("cheburek");
        assertTrue(results.isPresent());
    }

    @Test
    void updateAccount() {
        Optional<AccountDto> optionalLibrarian = accountService.findAccountById(6L);
        optionalLibrarian.ifPresent(librarian -> {
            librarian.setUsername("librarian");
            accountService.updateLibrarian(librarian);
        });
        Optional<AccountDto> librarianById = accountService.findAccountById(6L);
        librarianById.ifPresent(lib -> assertEquals("librarian", lib.getUsername()));
    }

    @Test
    void deleteAccount() {
        Optional<AccountDto> optionalLibrarian = accountService.findAccountById(5L);
        optionalLibrarian.ifPresent(librarian -> accountService.deleteAccount(librarian));
        Optional<AccountDto> librarianById = accountService.findAccountById(5L);
        assertFalse(librarianById.isPresent());
    }

    @Test
    void findByUsername() {
        Optional<AccountDto> lib = accountService.findAccountByUsername("l1ib");
        assertFalse(lib.isPresent());
    }
}