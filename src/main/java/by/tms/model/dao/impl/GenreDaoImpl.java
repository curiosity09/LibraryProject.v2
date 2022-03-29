package by.tms.model.dao.impl;

import by.tms.model.dao.GenreDao;
import by.tms.model.entity.Genre;
import by.tms.model.entity.Genre_;
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
public class GenreDaoImpl extends GenericDaoImpl<Long, Genre> implements GenreDao {

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
        Genre genre = session.createQuery(criteria).uniqueResult();
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_DAO_BY, genre, name);
        return Optional.of(genre);
    }
}
