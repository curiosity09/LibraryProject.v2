package by.tms.model.dao;

import by.tms.model.entity.user.Account;
import by.tms.model.entity.user.Admin;
import by.tms.model.entity.user.Librarian;
import by.tms.model.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface AccountDao extends GenericDao<Long, Account> {

    List<User> findAllUsers(int limit, int offset);

    List<User> findAllDebtors(int limit, int offset);

    List<Admin> findAllAdmins(int limit, int offset);

    List<Librarian> findAllLibrarians(int limit, int offset);

    Optional<Account> findByUsername(String username);

    Optional<Librarian> findLibrarianByUsername(String username);

    Optional<Librarian> findLibrarianById(Long id);

    Optional<Admin> findAdminById(Long id);

    Optional<Admin> findAdminByUsername(String username);

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserById(Long id);

    void blockUser(User user);

    void unblockUser(User user);
}
