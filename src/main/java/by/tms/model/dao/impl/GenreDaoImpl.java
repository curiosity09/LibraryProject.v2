package by.tms.model.dao.impl;

import by.tms.model.dao.GenreDao;
import by.tms.model.entity.Genre;
import by.tms.model.entity.Genre_;
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
public class GenreDaoImpl extends GenericDaoImpl<Long, Genre> implements GenreDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Genre> findByName(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Genre> criteria = cb.createQuery(Genre.class);
        Root<Genre> root = criteria.from(Genre.class);
        criteria.select(root).where(cb.equal(root.get(Genre_.name), name));
        List<Genre> results = entityManager.createQuery(criteria).getResultList();
        Genre genre = results.isEmpty() ? null : results.get(0);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_DAO_BY, genre, name);
        return Optional.ofNullable(genre);
    }
}
