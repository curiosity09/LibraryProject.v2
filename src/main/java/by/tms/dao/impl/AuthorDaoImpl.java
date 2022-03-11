package by.tms.dao.impl;

import by.tms.dao.AuthorDao;
import by.tms.entity.Author;
import by.tms.entity.Author_;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
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
        return Optional.ofNullable(session.createQuery(criteria).getSingleResult());
    }
}
