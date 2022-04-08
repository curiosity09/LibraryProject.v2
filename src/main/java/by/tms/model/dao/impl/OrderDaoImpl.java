package by.tms.model.dao.impl;

import by.tms.model.dao.OrderDao;
import by.tms.model.entity.Order;
import by.tms.model.entity.Order_;
import by.tms.model.entity.user.Account_;
import by.tms.model.entity.user.User;
import by.tms.model.util.LoggerUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@Slf4j
public class OrderDaoImpl extends GenericDaoImpl<Long, Order> implements OrderDao {

    @Override
    public List<Order> findAllByUsername(String username, int limit, int offset) {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Order> criteria = cb.createQuery(Order.class);
        Root<Order> from = criteria.from(Order.class);
        Join<Order, User> accountJoin = from.join(Order_.account);
        criteria.select(from).where(cb.equal(accountJoin.get(Account_.username), username));
        List<Order> resultList = session
                .createQuery(criteria)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_DAO_BY, resultList, username);
        return resultList;
    }

    @Override
    public Long getCountRow(String username) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Order> root = criteria.from(Order.class);
        Join<Order, User> accountJoin = root.join(Order_.account);
        Long singleResult = session
                .createQuery(criteria
                        .select(cb.count(root))
                        .where(cb.equal(accountJoin.get(Account_.username), username)))
                .getSingleResult();
        log.debug(LoggerUtil.COUNT_ROW_WAS_FOUND_IN_DAO, singleResult);
        return singleResult;
    }
}
