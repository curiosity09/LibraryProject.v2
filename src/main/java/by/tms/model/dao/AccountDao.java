package by.tms.model.dao;

import by.tms.model.entity.user.Account;
import by.tms.model.entity.user.Admin;
import by.tms.model.entity.user.Librarian;
import by.tms.model.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface AccountDao extends GenericDao<Long, Account> {

    Optional<Account> findByUsername(String username);

    void blockUser(User user);

    void unblockUser(User user);

    List<User> findAllDebtors();

    List<User> findAllUsers(int limit, int offset);

    List<Admin> findAllAdmins(int limit, int offset);

    List<Librarian> findAllLibrarians(int limit, int offset);

    Long getCountRow(Class<? extends Account> clazz);
}
