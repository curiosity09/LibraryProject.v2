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
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class AccountDaoImpl extends GenericDaoImpl<Long, Account> implements AccountDao {

    @Override
    public List<User> findAllUsers() {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> root = criteria.from(User.class);
        criteria.select(root);
        return session.createQuery(criteria).getResultList();
    }

    @Override
    public List<User> findAllDebtors() {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> from = criteria.from(User.class);
        ListJoin<User, Order> orderJoin = from.join(Account_.orders);
        criteria
                .select(from)
                .where(
                        cb.and(
                                cb.lessThan(orderJoin.get(Order_.rentalPeriod), LocalDateTime.now()),
                                cb.equal(from.get(User_.isBanned), false))
                ).distinct(true);
        return session.createQuery(criteria).getResultList();
    }

    @Override
    public List<Admin> findAllAdmins() {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Admin> criteria = cb.createQuery(Admin.class);
        Root<Admin> root = criteria.from(Admin.class);
        criteria.select(root);
        return session.createQuery(criteria).getResultList();
    }

    @Override
    public List<Librarian> findAllLibrarians() {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Librarian> criteria = cb.createQuery(Librarian.class);
        Root<Librarian> root = criteria.from(Librarian.class);
        criteria.select(root);
        return session.createQuery(criteria).getResultList();
    }

    @Override
    public Optional<Librarian> findLibrarianByUsername(String username) {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Librarian> criteria = cb.createQuery(Librarian.class);
        Root<Librarian> root = criteria.from(Librarian.class);
        criteria.select(root).where(cb.equal(root.get(Account_.username), username));
        return Optional.of(session.createQuery(criteria).getSingleResult());
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> root = criteria.from(User.class);
        criteria.select(root).where(cb.equal(root.get(Account_.username), username));
        return Optional.of(session.createQuery(criteria).getSingleResult());
    }

    @Override
    public Optional<User> findUserById(Long id) {
        Session session = getSessionFactory().getCurrentSession();
        return Optional.ofNullable(session.find(User.class, id));
    }

    @Override
    public Optional<Librarian> findLibrarianById(Long id) {
        Session session = getSessionFactory().getCurrentSession();
        return Optional.ofNullable(session.find(Librarian.class, id));
    }

    @Override
    public Optional<Admin> findAdminById(Long id) {
        Session session = getSessionFactory().getCurrentSession();
        return Optional.ofNullable(session.find(Admin.class, id));
    }

    @Override
    public void blockUser(User user) {
        Session session = getSessionFactory().getCurrentSession();
        if (Objects.equals(false, user.isBanned())) {
            user.setBanned(true);
            session.update(user);
        }
    }

    @Override
    public void unblockUser(User user) {
        Session session = getSessionFactory().getCurrentSession();
        if (Objects.equals(true, user.isBanned())) {
            user.setBanned(false);
            session.update(user);
        }
    }
}
