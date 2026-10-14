package by.tms.model.dao.impl;

import by.tms.model.dao.BookDao;
import by.tms.model.entity.Author;
import by.tms.model.entity.BaseEntity_;
import by.tms.model.entity.Book;
import by.tms.model.entity.Book_;
import by.tms.model.util.LoggerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class BookDaoImpl extends GenericDaoImpl<Long, Book> implements BookDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Book> findByName(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> criteria = cb.createQuery(Book.class);
        Root<Book> from = criteria.from(Book.class);
        criteria
                .select(from)
                .where(
                        cb.equal(from.get(Book_.name), name)
                );
        List<Book> resultList = entityManager.createQuery(criteria).getResultList();
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_DAO_BY, resultList, name);
        return resultList;
    }

    @Override
    public List<Book> findByAuthor(Long authorId, int limit, int offset) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> criteria = cb.createQuery(Book.class);
        Root<Book> from = criteria.from(Book.class);
        Join<Book, Author> join = from.join(Book_.author);
        criteria
                .select(from)
                .where(
                        cb.equal(join.get(BaseEntity_.id), authorId)
                );
        List<Book> resultList = entityManager
                .createQuery(criteria)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_DAO_BY, resultList, authorId);
        return resultList;
    }

    @Override
    public Long getCountRow(Long authorId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Book> root = criteria.from(Book.class);
        Join<Book, Author> authorJoin = root.join(Book_.author);
        Long singleResult = entityManager
                .createQuery(criteria
                        .select(cb.count(root))
                        .where(cb.equal(authorJoin.get(BaseEntity_.id), authorId)))
                .getSingleResult();
        log.debug(LoggerUtil.COUNT_ROW_WAS_FOUND_IN_DAO, singleResult);
        return singleResult;
    }
}
