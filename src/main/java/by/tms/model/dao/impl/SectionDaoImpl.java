package by.tms.model.dao.impl;

import by.tms.model.dao.SectionDao;
import by.tms.model.entity.Section;
import by.tms.model.entity.Section_;
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
public class SectionDaoImpl extends GenericDaoImpl<Long, Section> implements SectionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Section> findByName(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Section> criteria = cb.createQuery(Section.class);
        Root<Section> root = criteria.from(Section.class);
        criteria.select(root).where(cb.equal(root.get(Section_.name), name));
        List<Section> results = entityManager.createQuery(criteria).getResultList();
        Section section = results.isEmpty() ? null : results.get(0);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_DAO_BY, section, name);
        return Optional.ofNullable(section);
    }
}
