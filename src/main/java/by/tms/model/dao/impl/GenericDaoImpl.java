package by.tms.model.dao.impl;

import by.tms.model.dao.GenericDao;
import by.tms.model.entity.BaseEntity;
import by.tms.model.util.LoggerUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
@Component
@Slf4j
public abstract class GenericDaoImpl<P extends Serializable, E extends BaseEntity<P>> implements GenericDao<P, E> {

    protected SessionFactory sessionFactory;
    private final Class<E> clazz;

    protected GenericDaoImpl() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        clazz = (Class<E>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[1];
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<E> findById(P id) {
        Session session = sessionFactory.getCurrentSession();
        E entity = session.find(clazz, id);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_DAO_BY, entity, id);
        return Optional.ofNullable(entity);
    }

    @Override
    public P save(E entity) {
        Session session = sessionFactory.getCurrentSession();
        Serializable id = session.save(entity);
        log.debug(LoggerUtil.ENTITY_WAS_SAVED_IN_DAO, entity);
        return (P) id;
    }

    @Override
    public void update(E entity) {
        if (Objects.nonNull(entity)) {
            Session session = sessionFactory.getCurrentSession();
            session.merge(entity);
            log.debug(LoggerUtil.ENTITY_WAS_UPDATED_IN_DAO, entity);
        }
    }

    @Override
    public void delete(E entity) {
        if (Objects.nonNull(entity)) {
            Session session = sessionFactory.getCurrentSession();
            Object persistentInstance = session.load(clazz, entity.getId());
            if (persistentInstance != null) {
                session.delete(persistentInstance);
                log.debug(LoggerUtil.ENTITY_WAS_DELETED_IN_DAO, entity);
            }
        }
    }

    @Override
    public List<E> findAll(int limit, int offset) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<E> criteria = cb.createQuery(clazz);
        Root<E> root = criteria.from(clazz);
        List<E> resultList = session
                .createQuery(criteria.select(root))
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_DAO, resultList);
        return resultList;
    }

    @Override
    public boolean isExist(P id) {
        Session session = sessionFactory.getCurrentSession();
        E entity = session.get(clazz, id);
        log.debug(LoggerUtil.ENTITY_IS_EXIST_IN_DAO, entity);
        return Objects.nonNull(entity);
    }

    @Override
    public Long getCountRow() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<E> root = criteria.from(clazz);
        Long singleResult = session.createQuery(criteria.select(cb.count(root))).getSingleResult();
        log.debug(LoggerUtil.COUNT_ROW_WAS_FOUND_IN_DAO, singleResult);
        return singleResult;
    }
}
