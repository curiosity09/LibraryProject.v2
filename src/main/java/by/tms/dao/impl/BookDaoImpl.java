package by.tms.dao.impl;

import by.tms.dao.BookDao;
import by.tms.entity.Book;
import by.tms.entity.Book_;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BookDaoImpl implements BookDao {

    private static final BookDaoImpl INSTANCE = new BookDaoImpl();

    public static BookDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Book> findAll(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Book> criteria = cb.createQuery(Book.class);
        Root<Book> root = criteria.from(Book.class);
        criteria.select(root);
        return session.createQuery(criteria).getResultList();
    }

    @Override
    public Optional<Book> findById(Session session, Long id) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Book> criteria = cb.createQuery(Book.class);
        Root<Book> from = criteria.from(Book.class);
        criteria
                .select(from)
                .where(
                        cb.equal(from.get(Book_.id), id)
                );
        return Optional.of(session.createQuery(criteria).getSingleResult());
    }

    @Override
    public List<Book> findByName(Session session, String name) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Book> criteria = cb.createQuery(Book.class);
        Root<Book> from = criteria.from(Book.class);
        criteria
                .select(from)
                .where(
                        cb.equal(from.get(Book_.name), name)
                );
        return session.createQuery(criteria).getResultList();
    }

    @Override
    public void add(Session session, Book book) {
        session.save(book);
    }

    @Override
    public void update(Session session, Book book) {
        session.merge(book);
    }

    @Override
    public void delete(Session session, Book book) {
        session.delete(book);
    }
}
