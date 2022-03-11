package by.tms.service;

import by.tms.dto.OrderDto;
import by.tms.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    List<OrderDto> findAllOrder();

    Long addOrder(Order order);

    void deleteOrder(Order order);

    void updateOrder(Order order);

    boolean isOrderExist(Long id);

    List<OrderDto> findOrderByUsername(String username);

    Optional<OrderDto> findOrderById(Long id);
}
