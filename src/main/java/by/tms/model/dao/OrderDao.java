package by.tms.model.dao;

import by.tms.model.entity.Order;

import java.util.List;

public interface OrderDao extends GenericDao<Long, Order>{

    List<Order> findAllByUsername(String username, int limit, int offset);

    Long getCountRow(String username);
}
