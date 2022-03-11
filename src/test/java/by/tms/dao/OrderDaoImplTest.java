package by.tms.dao;

import by.tms.config.DatabaseConfigTest;
import by.tms.entity.Book;
import by.tms.entity.Order;
import by.tms.entity.user.User;
import by.tms.util.TestDataImporter;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.annotation.AfterTestMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
//@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DatabaseConfigTest.class)
@Transactional
class OrderDaoImplTest {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    public void initDb() {
        TestDataImporter.importTestData(sessionFactory);
    }

    @AfterTestMethod
    public void flush() {
        sessionFactory.close();
    }

    @Test
    void testFindAll() {
        List<Order> results = orderDao.findAll();
        assertEquals(3, results.size());
    }

    @Test
    void testFindById() {
        Optional<Order> byId = orderDao.findById(1L);
        byId.ifPresent(order -> assertEquals(2, order.getBook().size()));
    }

    @Test
    void testAdd() {
        User user = User.builder().build();
        user.setId(1L);
        Book book = Book.builder().build();
        book.setId(1L);
        Order order = Order.builder().book(List.of(book))
                .account(user).rentalTime(LocalDateTime.now()).rentalPeriod(LocalDateTime.MAX).build();
        Long id = orderDao.save(order);
        Optional<Order> byId = orderDao.findById(id);
        assertTrue(byId.isPresent());
        orderDao.delete(byId.get());
    }

    @Test
    void testUpdate() {
        Book book = Book.builder().build();
        book.setId(1L);
        Optional<Order> byId = orderDao.findById(1L);
        byId.ifPresent(order -> {
            order.setBook(List.of(book));
            orderDao.update(order);
        });
        Optional<Order> updatedOrder = orderDao.findById(1L);
        updatedOrder.ifPresent(order -> assertEquals(1, order.getBook().size()));
    }

    @Test
    void testDelete() {
        List<Order> allOrder = orderDao.findAll();
        orderDao.delete(allOrder.get(1));
        List<Order> orders = orderDao.findAll();
        assertEquals(1, orders.size());
    }

    @Test
    void findAllByUsername() {
        List<Order> byUsername = orderDao.findAllByUsername("user");
        assertEquals(2, byUsername.size());
    }
}