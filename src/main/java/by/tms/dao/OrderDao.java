package by.tms.dao;

import by.tms.entity.Order;

import java.util.List;

public interface OrderDao extends GenericDao<Long, Order>{

    List<Order> findAllByUsername(String username);
}
