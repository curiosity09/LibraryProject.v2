package by.tms.dao.impl;

import by.tms.dao.GenreDao;
import by.tms.entity.BaseEntity_;
import by.tms.entity.Genre;
import by.tms.entity.Genre_;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GenreDaoImpl implements GenreDao {

    private static final GenreDaoImpl INSTANCE = new GenreDaoImpl();

    public static GenreDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Genre> findAll(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Genre> criteria = cb.createQuery(Genre.class);
        Root<Genre> root = criteria.from(Genre.class);
        criteria.select(root);
        return session.createQuery(criteria).getResultList();
    }

    @Override
    public Optional<Genre> findById(Session session, Long id) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Genre> criteria = cb.createQuery(Genre.class);
        Root<Genre> from = criteria.from(Genre.class);
        criteria
                .select(from)
                .where(
                        cb.equal(from.get(BaseEntity_.id), id)
                );
        return Optional.of(session.createQuery(criteria).getSingleResult());
    }

    @Override
    public Optional<Genre> findByName(Session session, String name) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Genre> criteria = cb.createQuery(Genre.class);
        Root<Genre> from = criteria.from(Genre.class);
        criteria
                .select(from)
                .where(
                        cb.equal(from.get(Genre_.name), name)
                );
        return Optional.of(session.createQuery(criteria).getSingleResult());
    }

    @Override
    public void add(Session session, Genre genre) {
        session.save(genre);
    }

    @Override
    public void update(Session session, Genre genre) {
        session.merge(genre);
    }

    @Override
    public void delete(Session session, Genre genre) {
        session.delete(genre);
    }
}
