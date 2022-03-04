package by.tms.dao;

import by.tms.entity.Order;
import org.hibernate.Session;

import java.util.List;

public interface OrderDao extends GenericDao<Order>{

    List<Order> findAllByUsername(Session session, String username);
}
