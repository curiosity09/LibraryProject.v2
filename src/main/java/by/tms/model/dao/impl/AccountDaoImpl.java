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
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Repository
@Slf4j
public class AccountDaoImpl extends GenericDaoImpl<Long, Account> implements AccountDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findAllDebtors() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> from = criteria.from(User.class);
        Join<User, Order> orderJoin = from.join(Account_.orders);
        criteria
                .select(from)
                .where(
                        cb.and(
                                cb.lessThan(orderJoin.get(Order_.rentalPeriod), LocalDateTime.now()),
                                cb.equal(from.get(User_.isBanned), false))
                ).distinct(true);
        List<User> resultList = entityManager
                .createQuery(criteria)
                .getResultList();
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_DAO, resultList);
        return resultList;
    }

    @Override
    public List<User> findAllUsers(int limit, int offset) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> from = criteria.from(User.class);
        List<User> resultList = entityManager
                .createQuery(criteria.select(from))
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_DAO, resultList);
        return resultList;
    }

    @Override
    public List<Admin> findAllAdmins(int limit, int offset) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Admin> criteria = cb.createQuery(Admin.class);
        Root<Admin> from = criteria.from(Admin.class);
        List<Admin> resultList = entityManager
                .createQuery(criteria.select(from))
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_DAO, resultList);
        return resultList;
    }

    @Override
    public List<Librarian> findAllLibrarians(int limit, int offset) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Librarian> criteria = cb.createQuery(Librarian.class);
        Root<Librarian> from = criteria.from(Librarian.class);
        List<Librarian> resultList = entityManager
                .createQuery(criteria.select(from))
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_DAO, resultList);
        return resultList;
    }

    @Override
    public Optional<Account> findByUsername(String username) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Account> criteria = cb.createQuery(Account.class);
        Root<Account> root = criteria.from(Account.class);
        criteria.select(root).where(cb.equal(root.get(Account_.username), username));
        List<Account> results = entityManager.createQuery(criteria).getResultList();
        Account account = results.isEmpty() ? null : results.get(0);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_DAO_BY, account, username);
        return Optional.ofNullable(account);
    }

    @Override
    public void blockUser(User user) {
        if (Objects.equals(false, user.isBanned())) {
            user.setBanned(true);
            entityManager.merge(user);
            log.debug(LoggerUtil.USER_WAS_BLOCKED_IN_DAO, user);
        }
    }

    @Override
    public void unblockUser(User user) {
        if (Objects.equals(true, user.isBanned())) {
            user.setBanned(false);
            entityManager.merge(user);
            log.debug(LoggerUtil.USER_WAS_UNBLOCKED_IN_DAO, user);
        }
    }

    @Override
    public Long getCountRow(Class<? extends Account> clazz) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<? extends Account> from = criteria.from(clazz);
        Long singleResult = entityManager.createQuery(criteria.select(cb.count(from))).getSingleResult();
        log.debug(LoggerUtil.COUNT_ROW_WAS_FOUND_IN_DAO, singleResult);
        return singleResult;
    }
}
