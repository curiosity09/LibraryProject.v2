package by.tms.dao.impl;

import by.tms.dao.GenreDao;
import by.tms.entity.Genre;
import by.tms.entity.Genre_;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
public  class GenreDaoImpl extends GenericDaoImpl<Long, Genre> implements GenreDao {

    @Override
    public Optional<Genre> findByName(String name) {
        Session session = getSessionFactory().getCurrentSession();
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
}
