package by.tms.model.service.impl;

import by.tms.model.dao.GenericDao;
import by.tms.model.entity.BaseEntity;
import by.tms.model.mapper.Mapper;
import by.tms.model.service.GenericService;
import by.tms.model.util.LoggerUtil;
import by.tms.model.util.ServiceUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Getter
@Transactional(readOnly = true)
@Slf4j
public abstract class GenericServiceImpl<D extends Serializable, P extends Serializable, E extends BaseEntity<P>> implements GenericService<D, P, E> {

    @Autowired
    protected GenericDao<P, E> genericDao;
    private final Class<E> clazz;
    private final Class<D> dtoClazz;
    @Autowired
    protected Mapper<E, D> mapper;

    protected GenericServiceImpl() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        clazz = (Class<E>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[1];
        dtoClazz = (Class<D>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[1];
    }

    @Override
    public List<D> findAll(int limit, int offset) {
        List<E> all = genericDao.findAll(limit, offset);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_SERVICE, all);
        return mapper.mapToListDto(all);
    }

    @Transactional
    @Override
    public P save(D dto) {
        P save = genericDao.save(mapper.mapToEntity(dto));
        log.debug(LoggerUtil.ENTITY_WAS_SAVED_IN_SERVICE, save);
        return save;
    }

    @Transactional
    @Override
    public void update(D dto) {
        genericDao.update(mapper.mapToEntity(dto));
        log.debug(LoggerUtil.ENTITY_WAS_UPDATED_IN_SERVICE, dto);
    }

    @Transactional
    @Override
    public void delete(D dto) {
        genericDao.delete(mapper.mapToEntity(dto));
        log.debug(LoggerUtil.ENTITY_WAS_DELETED_IN_SERVICE, dto);
    }

    @Override
    public Optional<D> findById(P id) {
        Optional<E> byId = genericDao.findById(id);
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_SERVICE_BY, byId, id);
        return Optional.ofNullable(mapper.mapToDto(byId.orElse(null)));
    }

    @Override
    public boolean isExist(P id) {
        boolean exist = genericDao.isExist(id);
        log.debug(LoggerUtil.ENTITY_IS_EXIST_IN_SERVICE_BY, exist, id);
        return exist;
    }

    @Override
    public List<Long> getCountPages() {
        Long countRow = genericDao.getCountRow();
        log.debug(LoggerUtil.COUNT_ROW_WAS_FOUND_IN_SERVICE, countRow);
        return ServiceUtil.collectPages(countRow);
    }
}
