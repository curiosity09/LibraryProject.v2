package by.tms.model.dao.impl;

import by.tms.model.dao.BookDao;
import by.tms.model.entity.Author;
import by.tms.model.entity.BaseEntity_;
import by.tms.model.entity.Book;
import by.tms.model.entity.Book_;
import by.tms.model.util.LoggerUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@Slf4j
public class BookDaoImpl extends GenericDaoImpl<Long, Book> implements BookDao {

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
        List<Book> resultList = session.createQuery(criteria).getResultList();
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_DAO_BY, resultList, name);
        return resultList;
    }

    @Override
    public List<Book> findByAuthor(Long authorId, int limit, int offset) {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Book> criteria = cb.createQuery(Book.class);
        Root<Book> from = criteria.from(Book.class);
        Join<Book, Author> join = from.join(Book_.author);
        criteria
                .select(from)
                .where(
                        cb.equal(join.get(BaseEntity_.id), authorId)
                );
        List<Book> resultList = session
                .createQuery(criteria)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_DAO_BY, resultList, authorId);
        return resultList;
    }

    @Override
    public Long getCountRow(Long authorId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Book> root = criteria.from(Book.class);
        Join<Book, Author> authorJoin = root.join(Book_.author);
        Long singleResult = session
                .createQuery(criteria
                        .select(cb.count(root))
                        .where(cb.equal(authorJoin.get(BaseEntity_.id), authorId)))
                .getSingleResult();
        log.debug(LoggerUtil.COUNT_ROW_WAS_FOUND_IN_DAO, singleResult);
        return singleResult;
    }
}
