package by.tms.dao.impl;

import by.tms.dao.GenericDao;
import by.tms.entity.BaseEntity;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
public abstract class GenericDaoImpl<P extends Serializable, E extends BaseEntity<P>> implements GenericDao<P, E> {

    @Autowired
    protected SessionFactory sessionFactory;
    private final Class<E> clazz;

    protected GenericDaoImpl() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        clazz = (Class<E>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[1];
    }

    @Override
    public Optional<E> findById(P id) {
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.find(clazz, id));
    }

    @Override
    public P save(E entity) {
        Session session = sessionFactory.getCurrentSession();
        Serializable id = session.save(entity);
        return (P) id;
    }

    @Override
    public void update(E entity) {
        if (Objects.nonNull(entity)) {
            Session session = sessionFactory.getCurrentSession();
            session.merge(entity);
        }
    }

    @Override
    public void delete(E entity) {
        if (Objects.nonNull(entity)) {
            Session session = sessionFactory.getCurrentSession();
            Object persistentInstance = session.load(clazz, entity.getId());
            if (persistentInstance != null) {
                session.delete(persistentInstance);
            }
        }
    }

    @Override
    public List<E> findAll() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<E> criteria = cb.createQuery(clazz);
        Root<E> root = criteria.from(clazz);
        return session.createQuery(criteria.select(root)).getResultList();
    }

    @Override
    public boolean isExist(P id) {
        Session session = sessionFactory.getCurrentSession();
        return Objects.nonNull(session.get(clazz, id));
    }
}
