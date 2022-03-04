package by.tms.dao.impl;

import by.tms.dao.AuthorDao;
import by.tms.entity.Author;
import by.tms.entity.Author_;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthorDaoImpl implements AuthorDao {

    private static final AuthorDaoImpl INSTANCE = new AuthorDaoImpl();

    public static AuthorDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Author> findAll(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Author> criteria = cb.createQuery(Author.class);
        Root<Author> root = criteria.from(Author.class);
        criteria.select(root);
        return session.createQuery(criteria).getResultList();
    }

    @Override
    public Optional<Author> findById(Session session, Long id) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Author> criteria = cb.createQuery(Author.class);
        Root<Author> from = criteria.from(Author.class);
        criteria
                .select(from)
                .where(
                        cb.equal(from.get(Author_.id), id)
                );
        return Optional.of(session.createQuery(criteria).getSingleResult());
    }

    @Override
    public Optional<Author> findByFullName(Session session, String fullName) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Author> criteria = cb.createQuery(Author.class);
        Root<Author> from = criteria.from(Author.class);
        criteria
                .select(from)
                .where(
                        cb.equal(from.get(Author_.fullName), fullName)
                );
        return Optional.of(session.createQuery(criteria).getSingleResult());
    }

    @Override
    public void add(Session session, Author author) {
        session.save(author);
    }

    @Override
    public void update(Session session, Author author) {
        session.merge(author);
    }

    @Override
    public void delete(Session session, Author author) {
        session.delete(author);
    }
}
