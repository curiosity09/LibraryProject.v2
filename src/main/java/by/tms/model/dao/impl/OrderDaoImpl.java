package by.tms.model.dao.impl;

import by.tms.model.dao.OrderDao;
import by.tms.model.entity.Order;
import by.tms.model.entity.Order_;
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

import java.util.List;

@Repository
@Slf4j
public class OrderDaoImpl extends GenericDaoImpl<Long, Order> implements OrderDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Order> findAllByUsername(String username, int limit, int offset) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteria = cb.createQuery(Order.class);
        Root<Order> root = criteria.from(Order.class);
        Join<Order, User> accountJoin = root.join(Order_.account);
        criteria
                .select(root)
                .where(cb.equal(accountJoin.get(User_.username), username));
        List<Order> resultList = entityManager
                .createQuery(criteria)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_DAO_BY, resultList, username);
        return resultList;
    }

    @Override
    public Long getCountRow(String username) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Order> root = criteria.from(Order.class);
        Join<Order, User> accountJoin = root.join(Order_.account);
        Long singleResult = entityManager
                .createQuery(criteria
                        .select(cb.count(root))
                        .where(cb.equal(accountJoin.get(User_.username), username)))
                .getSingleResult();
        log.debug(LoggerUtil.COUNT_ROW_WAS_FOUND_IN_DAO, singleResult);
        return singleResult;
    }
}
