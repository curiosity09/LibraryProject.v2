package by.tms.dao;

import by.tms.dao.impl.OrderDaoImpl;
import by.tms.entity.Book;
import by.tms.entity.Order;
import by.tms.entity.user.User;
import by.tms.util.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public class OrderDaoImplTest {

    private final OrderDao orderDao = OrderDaoImpl.getInstance();
    private SessionFactory sessionFactory;

    @Before
    public void initDb() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
        TestDataImporter.getInstance().importTestData(sessionFactory);
    }

    @After
    public void finish() {
        sessionFactory.close();
    }

    @Test
    public void testFindAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Order> results = orderDao.findAll(session);
            Assert.assertEquals(2, results.size());
            session.getTransaction().commit();
        }
    }

    @Test
    public void testFindById() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Order> byId = orderDao.findById(session, 1L);
            byId.ifPresent(order -> Assert.assertEquals(2, order.getBook().size()));
            session.getTransaction().commit();
        }
    }

    @Test
    public void testAdd() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = User.builder().build();
            user.setId(1L);
            Book book = Book.builder().build();
            book.setId(1L);
            Order order = Order.builder().book(List.of(book))
                    .account(user).rentalTime(LocalDateTime.now()).rentalPeriod(LocalDateTime.MAX).build();
            orderDao.add(session, order);
            Optional<Order> byId = orderDao.findById(session, 2L);
            Assert.assertTrue(byId.isPresent());
            session.getTransaction().commit();
        }
    }

    @Test
    public void testUpdate() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Book book = Book.builder().build();
            book.setId(1L);
            Optional<Order> byId = orderDao.findById(session, 1L);
            byId.ifPresent(order -> {
                order.setBook(List.of(book));
                orderDao.update(session, order);
            });
            Optional<Order> updatedOrder = orderDao.findById(session, 1L);
            updatedOrder.ifPresent(order -> Assert.assertEquals(1, order.getBook().size()));
            session.getTransaction().commit();
        }
    }

    @Test
    public void testDelete() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Optional<Order> byId = orderDao.findById(session, 2L);
            byId.ifPresent(order -> orderDao.delete(session, order));
            List<Order> orders = orderDao.findAll(session);
            Assert.assertEquals(1, orders.size());
            transaction.commit();

        }
    }

    @Test
    public void findAllByUsername() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Order> byUsername = orderDao.findAllByUsername(session, "user");
            Assert.assertEquals(2, byUsername.size());
            session.getTransaction().commit();
        }
    }
}