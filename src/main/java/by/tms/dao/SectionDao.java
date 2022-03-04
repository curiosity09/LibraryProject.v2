package by.tms.dao;

import by.tms.entity.Section;
import org.hibernate.Session;

import java.util.Optional;

public interface SectionDao extends GenericDao<Section> {

    Optional<Section> findByName(Session session, String name);
}
