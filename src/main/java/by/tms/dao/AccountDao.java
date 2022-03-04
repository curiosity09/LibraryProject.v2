package by.tms.dao;

import by.tms.entity.user.Account;
import by.tms.entity.user.Admin;
import by.tms.entity.user.Librarian;
import by.tms.entity.user.User;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public interface AccountDao extends GenericDao<Account> {

    List<User> findAllUsers(Session session);

    List<User> findAllDebtors(Session session);

    boolean isExist(Session session, Long id);

    List<Admin> findAllAdmins(Session session);

    List<Librarian> findAllLibrarians(Session session);

    Optional<Account> findByUsername(Session session, String username);

    void blockUser(Session session, User user);

    void unblockUser(Session session, User user);
}
