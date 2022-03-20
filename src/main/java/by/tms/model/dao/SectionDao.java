package by.tms.dao;

import by.tms.entity.Section;

import java.util.Optional;

public interface SectionDao extends GenericDao<Long, Section> {

    Optional<Section> findByName(String name);
}
