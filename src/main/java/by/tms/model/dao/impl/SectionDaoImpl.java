package by.tms.model.dao.impl;

import by.tms.model.dao.SectionDao;
import by.tms.model.entity.Section;
import by.tms.model.entity.Section_;

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
public class SectionDaoImpl extends GenericDaoImpl<Long, Section> implements SectionDao {

    @Override
    public Optional<Section> findByName(String name) {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Section> criteria = cb.createQuery(Section.class);
        Root<Section> from = criteria.from(Section.class);
        criteria
                .select(from)
                .where(
                        cb.equal(from.get(Section_.name), name)
                );
        Section section = session.createQuery(criteria).uniqueResult();
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_DAO_BY, section, name);
        return Optional.ofNullable(section);
    }
}
