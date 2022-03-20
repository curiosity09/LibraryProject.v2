package by.tms.model.service;

import by.tms.model.dto.OrderDto;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    List<OrderDto> findAllOrder();

    Long addOrder(OrderDto orderDto);

    void deleteOrder(OrderDto orderDto);

    void updateOrder(OrderDto orderDto);

    boolean isOrderExist(Long id);

    List<OrderDto> findOrderByUsername(String username);

    Optional<OrderDto> findOrderById(Long id);
}
