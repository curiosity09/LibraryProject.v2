package by.tms.model.service;

import by.tms.model.dto.OrderDto;
import by.tms.model.entity.Order;

import java.util.List;

public interface OrderService extends GenericService<OrderDto,Long, Order> {

    List<OrderDto> findOrderByUsername(String username, int limit, int offset);

    List<Long> getCountPages(String username);
}
