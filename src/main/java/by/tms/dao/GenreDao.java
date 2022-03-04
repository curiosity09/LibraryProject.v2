package by.tms.dao;

import by.tms.entity.Genre;
import org.hibernate.Session;

import java.util.Optional;

public interface GenreDao extends GenericDao<Genre>{

    Optional<Genre> findByName(Session session, String name);
}
