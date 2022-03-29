package by.tms.model.dao.impl;

import by.tms.model.dao.AccountDao;
import by.tms.model.entity.Order;
import by.tms.model.entity.Order_;
import by.tms.model.entity.user.Account;
import by.tms.model.entity.user.Account_;
import by.tms.model.entity.user.Admin;
import by.tms.model.entity.user.Librarian;
import by.tms.model.entity.user.User;
import by.tms.model.entity.user.User_;
import by.tms.model.util.LoggerUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AccountDaoImpl extends GenericDaoImpl<Long, Account> implements AccountDao {

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
        List<User> resultList = session
                .createQuery(criteria)
                .getResultList();
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_DAO, resultList);
        return resultList;
    }

    @Override
    public List<User> findAllUsers(int limit, int offset) {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> from = criteria.from(User.class);
        List<User> resultList = session
                .createQuery(criteria.select(from))
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_DAO, resultList);
        return resultList;
    }

    @Override
    public List<Admin> findAllAdmins(int limit, int offset) {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Admin> criteria = cb.createQuery(Admin.class);
        Root<Admin> from = criteria.from(Admin.class);
        List<Admin> resultList = session
                .createQuery(criteria.select(from))
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_DAO, resultList);
        return resultList;
    }

    @Override
    public List<Librarian> findAllLibrarians(int limit, int offset) {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Librarian> criteria = cb.createQuery(Librarian.class);
        Root<Librarian> from = criteria.from(Librarian.class);
        List<Librarian> resultList = session
                .createQuery(criteria.select(from))
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_DAO, resultList);
        return resultList;
    }

    @Override
    public Optional<Account> findByUsername(String username) {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Account> criteria = cb.createQuery(Account.class);
        Root<Account> root = criteria.from(Account.class);
        criteria.select(root).where(cb.equal(root.get(Account_.username), username));
        Account account = session.createQuery(criteria).uniqueResult();
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_DAO_BY, account, username);
        return Optional.ofNullable(account);
    }

    @Override
    public void blockUser(User user) {
        Session session = getSessionFactory().getCurrentSession();
        if (Objects.equals(false, user.isBanned())) {
            user.setBanned(true);
            session.merge(user);
            log.debug(LoggerUtil.USER_WAS_BLOCKED_IN_DAO, user);
        }
    }

    @Override
    public void unblockUser(User user) {
        Session session = getSessionFactory().getCurrentSession();
        if (Objects.equals(true, user.isBanned())) {
            user.setBanned(false);
            session.merge(user);
            log.debug(LoggerUtil.USER_WAS_UNBLOCKED_IN_DAO, user);
        }
    }

    @Override
    public Long getCountRow(Class<? extends Account> clazz) {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<? extends Account> from = criteria.from(clazz);
        Long singleResult = session.createQuery(criteria.select(cb.count(from))).getSingleResult();
        log.debug(LoggerUtil.COUNT_ROW_WAS_FOUND_IN_DAO, singleResult);
        return singleResult;
    }
}
