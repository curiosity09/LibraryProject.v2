package by.tms.dao.impl;

import by.tms.dao.OrderDao;
import by.tms.entity.BaseEntity_;
import by.tms.entity.Order;
import by.tms.entity.Order_;
import by.tms.entity.user.Account;
import by.tms.entity.user.Account_;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OrderDaoImpl implements OrderDao {

    private static final OrderDaoImpl INSTANCE = new OrderDaoImpl();

    public static OrderDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Order> findAll(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Order> criteria = cb.createQuery(Order.class);
        Root<Order> from = criteria.from(Order.class);
        criteria.select(from);
        return session.createQuery(criteria).getResultList();
    }

    @Override
    public Optional<Order> findById(Session session, Long id) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Order> criteria = cb.createQuery(Order.class);
        Root<Order> from = criteria.from(Order.class);
        criteria
                .select(from)
                .where(
                        cb.equal(from.get(BaseEntity_.id), id)
                );
        return Optional.of(session.createQuery(criteria).getSingleResult());
    }

    @Override
    public List<Order> findAllByUsername(Session session, String username) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Order> criteria = cb.createQuery(Order.class);
        Root<Order> from = criteria.from(Order.class);
        Join<Order, Account> accountJoin = from.join(Order_.account);
        criteria.select(from).where(cb.equal(accountJoin.get(Account_.username), username));
        return session.createQuery(criteria).getResultList();
    }

    @Override
    public void add(Session session, Order order) {
        session.save(order);
    }

    @Override
    public void delete(Session session, Order order) {
        session.delete(order);
    }

    @Override
    public void update(Session session, Order order) {
        session.update(order);
    }
}
