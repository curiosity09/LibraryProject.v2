package by.tms.dao.impl;

import by.tms.dao.SectionDao;
import by.tms.entity.Section;
import by.tms.entity.Section_;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
public  class SectionDaoImpl extends GenericDaoImpl<Long, Section> implements SectionDao {

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
        return Optional.of(session.createQuery(criteria).getSingleResult());
    }
}
