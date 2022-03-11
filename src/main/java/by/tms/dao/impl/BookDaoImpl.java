package by.tms.dao.impl;

import by.tms.dao.BookDao;
import by.tms.entity.Book;
import by.tms.entity.Book_;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public  class BookDaoImpl extends GenericDaoImpl<Long, Book> implements BookDao {

    @Override
    public List<Book> findByName(String name) {
        Session session = getSessionFactory().getCurrentSession();
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
}
