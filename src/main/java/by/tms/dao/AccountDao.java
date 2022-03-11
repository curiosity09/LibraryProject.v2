package by.tms.dao;

import by.tms.entity.user.Account;
import by.tms.entity.user.Admin;
import by.tms.entity.user.Librarian;
import by.tms.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface AccountDao extends GenericDao<Long, Account> {

    List<User> findAllUsers();

    List<User> findAllDebtors();

    List<Admin> findAllAdmins();

    List<Librarian> findAllLibrarians();

    Optional<Librarian> findLibrarianByUsername(String username);

    Optional<Librarian> findLibrarianById(Long id);

    Optional<Admin> findAdminById(Long id);

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserById(Long id);

    void blockUser(User user);

    void unblockUser(User user);
}
