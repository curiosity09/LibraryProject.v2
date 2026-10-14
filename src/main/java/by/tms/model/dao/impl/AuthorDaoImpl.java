package by.tms.model.dao.impl;

import by.tms.model.dao.AuthorDao;
import by.tms.model.entity.Author;
import by.tms.model.entity.Author_;
import by.tms.model.util.LoggerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@Slf4j
public class AuthorDaoImpl extends GenericDaoImpl<Long, Author> implements AuthorDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Author> findByFullName(String fullName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Author> criteria = cb.createQuery(Author.class);
        Root<Author> root = criteria.from(Author.class);
        criteria.select(root).where(cb.equal(root.get(Author_.fullName), fullName));
        List<Author> results = entityManager.createQuery(criteria).getResultList();
        Author author = results.isEmpty() ? null : results.get(0);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_DAO_BY, author, fullName);
        return Optional.ofNullable(author);
    }
}
