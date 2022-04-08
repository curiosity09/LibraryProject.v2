package by.tms.model.dao.impl;

import by.tms.model.dao.AuthorDao;
import by.tms.model.entity.Author;
import by.tms.model.entity.Author_;
import by.tms.model.util.LoggerUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
@Slf4j
public class AuthorDaoImpl extends GenericDaoImpl<Long, Author> implements AuthorDao {

    @Override
    public Optional<Author> findByFullName(String fullName) {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Author> criteria = cb.createQuery(Author.class);
        Root<Author> from = criteria.from(Author.class);
        criteria
                .select(from)
                .where(
                        cb.equal(from.get(Author_.fullName), fullName)
                );
        Author author = session.createQuery(criteria).uniqueResult();
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_DAO_BY, author, fullName);
        return Optional.ofNullable(author);
    }
}
