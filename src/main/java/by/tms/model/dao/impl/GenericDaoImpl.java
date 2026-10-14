package by.tms.model.dao.impl;

import by.tms.model.dao.GenericDao;
import by.tms.model.entity.BaseEntity;
import by.tms.model.util.LoggerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public abstract class GenericDaoImpl<P extends Serializable, E extends BaseEntity<P>> implements GenericDao<P, E> {

    @PersistenceContext
    protected EntityManager entityManager;

    private final Class<E> clazz;

    protected GenericDaoImpl() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        clazz = (Class<E>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[1];
    }

    @Override
    public Optional<E> findById(P id) {
        E entity = entityManager.find(clazz, id);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_DAO_BY, entity, id);
        return Optional.ofNullable(entity);
    }

    @Override
    public P save(E entity) {
        entityManager.persist(entity);
        log.debug(LoggerUtil.ENTITY_WAS_SAVED_IN_DAO, entity);
        return entity.getId();
    }

    @Override
    public void update(E entity) {
        if (Objects.nonNull(entity)) {
            entityManager.merge(entity);
            log.debug(LoggerUtil.ENTITY_WAS_UPDATED_IN_DAO, entity);
        }
    }

    @Override
    public void delete(E entity) {
        if (Objects.nonNull(entity)) {
            E managedEntity = entityManager.find(clazz, entity.getId());
            if (managedEntity != null) {
                entityManager.remove(managedEntity);
                log.debug(LoggerUtil.ENTITY_WAS_DELETED_IN_DAO, entity);
            }
        }
    }

    @Override
    public List<E> findAll(int limit, int offset) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> criteria = cb.createQuery(clazz);
        Root<E> root = criteria.from(clazz);
        List<E> resultList = entityManager
                .createQuery(criteria.select(root))
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_DAO, resultList);
        return resultList;
    }

    @Override
    public boolean isExist(P id) {
        E entity = entityManager.find(clazz, id);
        log.debug(LoggerUtil.ENTITY_IS_EXIST_IN_DAO, entity);
        return Objects.nonNull(entity);
    }

    @Override
    public Long getCountRow() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<E> root = criteria.from(clazz);
        Long singleResult = entityManager.createQuery(criteria.select(cb.count(root))).getSingleResult();
        log.debug(LoggerUtil.COUNT_ROW_WAS_FOUND_IN_DAO, singleResult);
        return singleResult;
    }
}
