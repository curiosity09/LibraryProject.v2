package by.tms.model.dao;

import by.tms.model.entity.Section;

import java.util.Optional;

public interface SectionDao extends GenericDao<Long, Section> {

    Optional<Section> findByName(String name);
}
