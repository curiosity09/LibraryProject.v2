package by.tms.dao.impl;

import by.tms.dao.AccountDao;
import by.tms.entity.BaseEntity_;
import by.tms.entity.Order;
import by.tms.entity.Order_;
import by.tms.entity.user.Account;
import by.tms.entity.user.Account_;
import by.tms.entity.user.Admin;
import by.tms.entity.user.Librarian;
import by.tms.entity.user.User;
import by.tms.entity.user.User_;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AccountDaoImpl implements AccountDao {

    private static final AccountDaoImpl INSTANCE = new AccountDaoImpl();

    public static AccountDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Account> findAll(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Account> criteria = cb.createQuery(Account.class);
        Root<Account> root = criteria.from(Account.class);
        criteria.select(root);
        return session.createQuery(criteria).getResultList();
    }

    @Override
    public List<User> findAllUsers(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> root = criteria.from(User.class);
        criteria.select(root);
        return session.createQuery(criteria).getResultList();
    }

    @Override
    public Optional<Account> findById(Session session, Long id) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Account> criteria = cb.createQuery(Account.class);
        Root<Account> from = criteria.from(Account.class);
        criteria
                .select(from)
                .where(
                        cb.equal(from.get(BaseEntity_.id), id)
                );
        return Optional.of(session.createQuery(criteria).getSingleResult());
    }

    @Override
    public List<User> findAllDebtors(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> from = criteria.from(User.class);
        ListJoin<User, Order> orderJoin = from.join(Account_.orders);
        criteria
                .select(from)
                .where(
                        cb.and(
                                cb.greaterThan(orderJoin.get(Order_.rentalPeriod), LocalDateTime.now()),
                                cb.equal(from.get(User_.isBanned), false))
                ).distinct(true);
        return session.createQuery(criteria).getResultList();
    }

    @Override
    public boolean isExist(Session session, Long id) {
        return findById(session, id).isPresent();
    }

    @Override
    public List<Admin> findAllAdmins(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Admin> criteria = cb.createQuery(Admin.class);
        Root<Admin> root = criteria.from(Admin.class);
        criteria.select(root);
        return session.createQuery(criteria).getResultList();
    }

    @Override
    public List<Librarian> findAllLibrarians(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Librarian> criteria = cb.createQuery(Librarian.class);
        Root<Librarian> root = criteria.from(Librarian.class);
        criteria.select(root);
        return session.createQuery(criteria).getResultList();
    }

    @Override
    public Optional<Account> findByUsername(Session session, String username) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Account> criteria = cb.createQuery(Account.class);
        Root<Account> root = criteria.from(Account.class);
        criteria.select(root).where(cb.equal(root.get(Account_.username), username));
        return Optional.of(session.createQuery(criteria).getSingleResult());
    }

    @Override
    public void add(Session session, Account account) {
        session.save(account);
    }

    @Override
    public void update(Session session, Account account) {
        session.merge(account);
    }

    @Override
    public void blockUser(Session session, User user) {
        if (Objects.equals(false, user.isBanned())) {
            user.setBanned(true);
            session.merge(user);
        }
    }

    @Override
    public void unblockUser(Session session, User user) {
        if (Objects.equals(true, user.isBanned())) {
            user.setBanned(false);
            session.merge(user);
        }
    }

    @Override
    public void delete(Session session, Account account) {
        session.delete(account);
    }
}
