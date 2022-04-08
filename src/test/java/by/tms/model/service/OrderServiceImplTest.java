package by.tms.model.service;

import by.tms.model.config.DatabaseConfigTest;
import by.tms.model.dto.OrderDto;
import by.tms.model.util.TestDataImporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static by.tms.model.util.TestDataImporter.LIMIT_10;
import static by.tms.model.util.TestDataImporter.OFFSET_0;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DatabaseConfigTest.class)
@Transactional
class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private TestDataImporter testDataImporter;

    @BeforeEach
    public void initDb() {
        testDataImporter.cleanTestData();
        testDataImporter.importTestData();
    }

    @Test
    void findAllOrder() {
        List<OrderDto> allOrder = orderService.findAll(LIMIT_10, OFFSET_0);
        assertEquals(2, allOrder.get(0).getBookList().size());
    }

    @Test
    void addOrder() {
        OrderDto order = OrderDto.builder().bookList(null)
                .user(null).rentalTime(LocalDateTime.now()).rentalPeriod(LocalDateTime.MAX).build();
        Long id = orderService.save(order);
        Optional<OrderDto> byId = orderService.findById(id);
        assertTrue(byId.isPresent());
    }

    @Test
    void deleteOrder() {
        Optional<OrderDto> byId = orderService.findById(3L);
        byId.ifPresent(order -> orderService.delete(order));
        Optional<OrderDto> orderById = orderService.findById(3L);
        assertFalse(orderById.isPresent());
    }

    @Test
    void updateOrder() {
        Optional<OrderDto> byId = orderService.findById(2L);
        byId.ifPresent(order -> {
            order.setRentalTime(LocalDateTime.of(2020, 12, 31, 10, 12));
            orderService.update(order);
        });
        Optional<OrderDto> orderById = orderService.findById(2L);
        orderById.ifPresent(orderDto -> assertEquals(
                LocalDateTime.of(2020, 12, 31, 10, 12),
                orderDto.getRentalTime()));
    }

    @Test
    void findOrderByUsername() {
        List<OrderDto> orderByUsername = orderService.findOrderByUsername("user", LIMIT_10, OFFSET_0);
        assertEquals(1, orderByUsername.size());
    }

    @Test
    void findOrderById() {
        Optional<OrderDto> orderById = orderService.findById(2L);
        orderById.ifPresent(orderDto -> assertEquals("newUser", orderDto.getUser().getUsername()));
    }
}