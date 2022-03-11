package by.tms.mapper;

import java.util.List;

public interface Mapper<E, D> {

    D mapToDto(E entity);

    List<D> mapToListDto(List<E> entities);
}
