package by.tms.dao.impl;

import by.tms.dao.SectionDao;
import by.tms.entity.Section;
import by.tms.entity.Section_;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SectionDaoImpl implements SectionDao {

    private static final SectionDaoImpl INSTANCE = new SectionDaoImpl();

    public static SectionDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Section> findAll(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Section> criteria = cb.createQuery(Section.class);
        Root<Section> root = criteria.from(Section.class);
        criteria.select(root);
        return session.createQuery(criteria).getResultList();
    }

    @Override
    public Optional<Section> findById(Session session, Long id) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Section> criteria = cb.createQuery(Section.class);
        Root<Section> from = criteria.from(Section.class);
        criteria
                .select(from)
                .where(
                        cb.equal(from.get(Section_.id), id)
                );
        return Optional.of(session.createQuery(criteria).getSingleResult());
    }

    @Override
    public Optional<Section> findByName(Session session, String name) {
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

    @Override
    public void add(Session session, Section section) {
        session.save(section);
    }

    @Override
    public void update(Session session, Section section) {
        session.merge(section);
    }

    @Override
    public void delete(Session session, Section section) {
        session.delete(section);
    }
}
