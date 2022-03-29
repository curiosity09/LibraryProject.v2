package by.tms.model.service;

import by.tms.model.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface GenericService<D extends Serializable, P extends Serializable, E extends BaseEntity<P>> {

    List<D> findAll(int limit, int offset);

    P save(D dto);

    void update(D dto);

    void delete(D dto);

    Optional<D> findById(P id);

    boolean isExist(P id);

    List<Long> getCountPages();
}
