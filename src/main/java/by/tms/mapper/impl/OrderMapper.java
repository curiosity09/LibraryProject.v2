package by.tms.mapper.impl;

import by.tms.dto.OrderDto;
import by.tms.entity.Order;
import by.tms.mapper.Mapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderMapper implements Mapper<Order, OrderDto> {

    private static final OrderMapper INSTANCE = new OrderMapper();
    private final BookMapper bookMapper = BookMapper.getInstance();
    private final AccountMapper accountMapper = AccountMapper.getInstance();

    public static OrderMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public OrderDto mapToDto(Order order) {
        if (Objects.nonNull(order)) {
            return OrderDto.builder()
                    .id(order.getId())
                    .user(accountMapper.mapUserToDto(order.getAccount()))
                    .rentalTime(order.getRentalTime())
                    .rentalPeriod(order.getRentalPeriod())
                    .bookList(bookMapper.mapToListDto(order.getBook()))
                    .build();
        }
        return null;
    }

    @Override
    public List<OrderDto> mapToListDto(List<Order> orders) {
        if(Objects.nonNull(orders)) {
            List<OrderDto> orderDtoList = new ArrayList<>();
            for (Order order : orders) {
                orderDtoList.add(mapToDto(order));
            }
            return orderDtoList;
        }
        return Collections.emptyList();
    }
}
