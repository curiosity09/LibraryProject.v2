package by.tms.model.service.impl;

import by.tms.model.dao.OrderDao;
import by.tms.model.dto.OrderDto;
import by.tms.model.entity.Order;
import by.tms.model.mapper.impl.OrderMapper;
import by.tms.model.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final OrderMapper orderMapper = OrderMapper.getInstance();

    @Override
    public List<OrderDto> findAllOrder(int limit, int offset) {
        List<Order> orders = orderDao.findAll(limit, offset);
        return orderMapper.mapToListDto(orders);
    }

    @Override
    @Transactional
    public Long addOrder(OrderDto orderDto) {
        return orderDao.save(orderMapper.mapToEntity(orderDto));
    }

    @Override
    @Transactional
    public void deleteOrder(OrderDto orderDto) {
        orderDao.delete(orderMapper.mapToEntity(orderDto));
    }

    @Override
    @Transactional
    public void updateOrder(OrderDto orderDto) {
        orderDao.update(orderMapper.mapToEntity(orderDto));
    }

    @Override
    public boolean isOrderExist(Long id) {
        return orderDao.isExist(id);
    }

    @Override
    public List<OrderDto> findOrderByUsername(String username, int limit, int offset) {
        List<Order> orders = orderDao.findAllByUsername(username, limit, offset);
        return orderMapper.mapToListDto(orders);
    }

    @Override
    public Optional<OrderDto> findOrderById(Long id) {
        Optional<Order> optionalOrder = orderDao.findById(id);
        return Optional.ofNullable(orderMapper.mapToDto(optionalOrder.orElse(null)));
    }
}
