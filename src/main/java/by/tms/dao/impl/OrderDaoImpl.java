package by.tms.dao.impl;

import by.tms.dao.OrderDao;
import by.tms.entity.Order;
import by.tms.entity.Order_;
import by.tms.entity.user.Account_;
import by.tms.entity.user.User;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public  class OrderDaoImpl extends GenericDaoImpl<Long, Order> implements OrderDao {

    @Override
    public List<Order> findAllByUsername(String username) {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Order> criteria = cb.createQuery(Order.class);
        Root<Order> from = criteria.from(Order.class);
        Join<Order, User> accountJoin = from.join(Order_.account);
        criteria.select(from).where(cb.equal(accountJoin.get(Account_.username), username));
        return session.createQuery(criteria).getResultList();
    }
}
