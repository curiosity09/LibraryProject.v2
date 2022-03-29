package by.tms.model.mapper;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface Mapper<E, D> {

    D mapToDto(E entity);

    List<D> mapToListDto(List<E> entities);

    E mapToEntity(D dto);
}
