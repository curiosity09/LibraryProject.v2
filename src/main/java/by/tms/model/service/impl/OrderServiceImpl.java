package by.tms.model.service.impl;

import by.tms.model.dao.OrderDao;
import by.tms.model.dto.OrderDto;
import by.tms.model.entity.Order;
import by.tms.model.mapper.impl.OrderMapper;
import by.tms.model.service.OrderService;
import by.tms.model.util.LoggerUtil;
import by.tms.model.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final OrderMapper orderMapper = OrderMapper.getInstance();

    @Override
    public List<OrderDto> findAllOrder(int limit, int offset) {
        List<Order> orders = orderDao.findAll(limit, offset);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_SERVICE, orders);
        return orderMapper.mapToListDto(orders);
    }

    @Override
    @Transactional
    public Long addOrder(OrderDto orderDto) {
        Long saveOrder = orderDao.save(orderMapper.mapToEntity(orderDto));
        log.debug(LoggerUtil.ENTITY_WAS_SAVED_IN_SERVICE, saveOrder);
        return saveOrder;
    }

    @Override
    @Transactional
    public void deleteOrder(OrderDto orderDto) {
        orderDao.delete(orderMapper.mapToEntity(orderDto));
        log.debug(LoggerUtil.ENTITY_WAS_DELETED_IN_SERVICE, orderDto);
    }

    @Override
    @Transactional
    public void updateOrder(OrderDto orderDto) {
        orderDao.update(orderMapper.mapToEntity(orderDto));
        log.debug(LoggerUtil.ENTITY_WAS_UPDATED_IN_SERVICE, orderDto);
    }

    @Override
    public boolean isOrderExist(Long id) {
        boolean exist = orderDao.isExist(id);
        log.debug(LoggerUtil.ENTITY_IS_EXIST_IN_SERVICE_BY, exist, id);
        return exist;
    }

    @Override
    public List<OrderDto> findOrderByUsername(String username, int limit, int offset) {
        List<Order> orders = orderDao.findAllByUsername(username, limit, offset);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_SERVICE_BY, orders, username);
        return orderMapper.mapToListDto(orders);
    }

    @Override
    public Optional<OrderDto> findOrderById(Long id) {
        Optional<Order> optionalOrder = orderDao.findById(id);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_SERVICE_BY, optionalOrder, id);
        return Optional.ofNullable(orderMapper.mapToDto(optionalOrder.orElse(null)));
    }

    @Override
    public List<Long> getCountPages() {
        Long countRow = orderDao.getCountRow();
        log.debug(LoggerUtil.COUNT_ROW_WAS_FOUND_IN_SERVICE, countRow);
        return ServiceUtil.collectPages(countRow);
    }

    @Override
    public List<Long> getCountPages(String username) {
        Long countRow = orderDao.getCountRow(username);
        log.debug(LoggerUtil.COUNT_ROW_WAS_FOUND_IN_SERVICE, countRow);
        return ServiceUtil.collectPages(countRow);
    }
}
